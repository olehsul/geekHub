package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.ConversationDao;
import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserConversationDao;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.*;
import com.owu.geekhub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Lists;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class MessageController {
    private final UserDao userDao;
    private final MessageDAO messageDAO;
    private final MessageService messageService;
    private final UserConversationDao userConversationDao;
    private final ConversationDao conversationDao;
    private final SimpMessagingTemplate template;

    @Autowired
    public MessageController(UserDao userDao, MessageDAO messageDAO, MessageService messageService, UserConversationDao userConversationDao, ConversationDao conversationDao, SimpMessagingTemplate template) {
        this.userDao = userDao;
        this.messageDAO = messageDAO;
        this.messageService = messageService;
        this.userConversationDao = userConversationDao;
        this.conversationDao = conversationDao;
        this.template = template;
    }


    @GetMapping("/chat/conversations-for-{username}")
    public List<Conversation> getConversations(@PathVariable String username) {
        User user = userDao.findByUsername(username);
        List<Conversation> collected = user.getConversations()
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.getTheLastMessage() == null && o2.getTheLastMessage() == null)
                        return 0;
                    else if (o1.getTheLastMessage() == null && o2.getTheLastMessage() != null)
                        return -1;
                    else if (o2.getTheLastMessage() == null && o1.getTheLastMessage() != null)
                        return 1;
                    else
                        return o1.getTheLastMessage().getDate().compareTo(o2.getTheLastMessage().getDate());
                })
                .collect(Collectors.toList());
        Collections.reverse(collected);
        return collected;
    }

    @GetMapping("/chat/messages-for-{conversationId}")
    public List<Message> getMessages(@PathVariable Long conversationId) {
        return messageDAO.findAllByConversationId(conversationId)
                .stream()
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
    }


    // mapping for sent message to show both users & update conversations list
    @MessageMapping("/private-message")
    public void privateMessage(OutgoingMessage outgoingMessage) {
        if (outgoingMessage.getContent() == null) {
            return;
        }

        System.out.println("Outgoing message: " + outgoingMessage);
        Conversation conversation = conversationDao.findById(outgoingMessage.getConversationId()).get();

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal();
        User principal = userDao.findByUsername(outgoingMessage.getSenderUsername());

        Message message = Message.builder()
                .content(outgoingMessage.getContent())
                .sender(principal)
                .date(ZonedDateTime.now())
//                .time(new Time(System.currentTimeMillis()))
                .conversationId(conversation.getId())
                .unreadByUser(userDao.findByUsername(outgoingMessage.getRecipientUsername()))
                .parentMessageId(conversation.getTheLastMessage() != null ? conversation.getTheLastMessage().getId() : null)
                .build();

        messageDAO.save(message);

        conversation.setTheLastMessage(message);
        conversationDao.save(conversation);

        template.convertAndSend("/chat/update-conversation-for-" + principal.getUsername(), conversationDao.findById(outgoingMessage.getConversationId()).get());
        template.convertAndSend("/chat/update-conversation-for-" + outgoingMessage.getRecipientUsername(), conversationDao.findById(outgoingMessage.getConversationId()).get());
        template.convertAndSend("/chat/private-messages-for-conversation-id" + outgoingMessage.getConversationId(), message);

    }

    @MessageMapping("/set-messages-as-read-in-conversation-{conversationId}-for-{receiver}")
    public void saveMessageAsRead(
            @DestinationVariable Long conversationId,
            @DestinationVariable String receiver
//            @DestinationVariable String sender
    ) {
        List<Message> readMessages = new ArrayList<>();

        List<Message> unreadMessages = messageDAO
                .findDistinctByUnreadByUsersIn(Collections
                        .singletonList(userDao.findByUsername(receiver))
                );

        for (Message message : unreadMessages) {
            Iterator<User> iterator = message.getUnreadByUsers().iterator();
            while (iterator.hasNext()) {
                User notSeenByUser = iterator.next();
                if (notSeenByUser.getUsername().equals(receiver)) {
                    readMessages.add(message);
                    iterator.remove();
                }
            }
            messageDAO.save(message);
        }

        // todo: solve send to all users, or only sender
        if (readMessages.size() > 0) {
            template.convertAndSend("/chat/get-read-messages-in-conversation-" + conversationId
//                    + "-for-" + receiver
                    , readMessages);
        }
    }

    @GetMapping("/api/unread-messages")
    public List<Message> getUnreadMessages(@RequestParam String username) {
        return this.userDao.findByUsername(username).getUnreadMessages();
    }

    @PostMapping("/goto-conversation")
    public Conversation createConversationIfNotExistsAndGet(
            @RequestParam Long friendId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Conversation conversation = null;
        try {
            messageService.createConversationIfNotExists(principal.getId(), friendId);
            List<UserConversation> friendConversations = userConversationDao.findAllByUser_id(friendId);
            List<UserConversation> principalConversations = userConversationDao.findAllByUser_id(principal.getId());
            for (UserConversation friendConversation : friendConversations) {
                for (UserConversation principalConversation : principalConversations) {
                    if (friendConversation.getConversation_id().equals(principalConversation.getConversation_id())) {
                        conversation = conversationDao.findById(principalConversation.getConversation_id()).get();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conversation;
    }
}

