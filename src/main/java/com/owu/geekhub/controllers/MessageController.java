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

import java.time.ZonedDateTime;
import java.util.*;

@Controller
public class MessageController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserConversationDao userConversationDao;
    @Autowired
    private ConversationDao conversationDao;
    @Autowired
    private SimpMessagingTemplate template;

    // mapping for request to load conversation lost for user
    @MessageMapping("/conversations-request-for-{username}")
    @SendTo("/chat/requested-conversations-for-{username}")
    public List<Conversation> getConversations(@DestinationVariable String username) {
        System.out.println("INSIDE CONVERSATION MESSAGE MAPPING");
        User user = userDao.findByUsername(username);
        System.out.println(user);

        List<Conversation> conversations = user.getConversations();

        System.out.println(conversations.size());
        return conversations;
    }

    // mapping for request to load messages for selected conversation
    @MessageMapping("/messages-for-conversation-id{conversationId}")
    @SendTo("/chat/messages-list-for-conversation-id{conversationId}")
    public List<Message> getMessages(@DestinationVariable Long conversationId) {
        System.out.println("INSIDE GET-MESSAGES______________________________________");
        List<Message> list = messageDAO.findAllByConversation_Id(conversationId);
        for (Message message : list) {
            System.out.println(message);
        }
        return list;
    }

    // mapping for sent message to show both users & update conversations list
    @MessageMapping("/private-message")
    public void privateMessage(OutgoingMessage outgoingMessage) {
        if (outgoingMessage.getContent() == null) {
            System.out.println("Can not send empty message!__________________");
            return;
        }

        System.out.println(outgoingMessage);
        System.out.println("INSIDE SEND PRIVATE MSG_________________________________");
        Conversation conversation = conversationDao.findById(outgoingMessage.getConversationId()).get();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal();
        User principal = userDao.findByUsername(outgoingMessage.getSenderUsername());
        Message message = Message.builder()
                .content(outgoingMessage.getContent())
                .sender(principal)
                .date(ZonedDateTime.now())
//                .time(new Time(System.currentTimeMillis()))
                .conversation(conversation)
                .parentMessage(conversation.getTheLastMessage())
                .build();
        messageDAO.save(message);

        conversation.setTheLastMessage(message);

        conversationDao.save(conversation);

        template.convertAndSend("/chat/update-conversation-for-" + principal.getUsername(), conversationDao.findById(outgoingMessage.getConversationId()).get());
        template.convertAndSend("/chat/update-conversation-for-" + outgoingMessage.getRecipientUsername(), conversationDao.findById(outgoingMessage.getConversationId()).get());
        template.convertAndSend("/chat/messages-list-for-conversation-id" + outgoingMessage.getConversationId(), messageDAO.findAllByConversation_Id(outgoingMessage.getConversationId()));

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/goto-conversation")
    @ResponseBody
    public Conversation createConversationOrMessage(
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
