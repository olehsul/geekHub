package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.ConversationDao;
import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserConversationDao;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.*;
import com.owu.geekhub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserConversationDao userConversationDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void save(Message msg) {

    }

    @Override
    public void createConversationIfNotExists(Long userId, Long friendId) {
        List<UserConversation> allConvByUserId = userConversationDao.findAllByUser_id(userId);
        List<UserConversation> allConvByFriendId = userConversationDao.findAllByUser_id(friendId);
        boolean conversationExists = false;
        for (UserConversation userConversation : allConvByFriendId) {
            for (UserConversation conversation : allConvByUserId) {
                if (userConversation.getConversation_id().equals(conversation.getConversation_id())) {
                    conversationExists = true;
                    break;
                }
            }

            if (conversationExists)
                break;
        }

        if (!conversationExists) {
            User user = userDao.findById(userId).get();
            User friend = userDao.findById(friendId).get();
            Conversation conversation = new Conversation();
            conversation.setUsers(new ArrayList<>());
            conversation.getUsers().add(user);
            conversation.getUsers().add(friend);
            conversation.setMessages(new ArrayList<>());
            conversationDao.save(conversation);
        }
    }

    public Conversation gotoConversation(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Conversation conversation = null;
        try {
            createConversationIfNotExists(principal.getId(), friendId);
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

    @Override
    public List<Conversation> getConversations(String username) {
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

    @Override
    public void saveMessageAsRead(Long conversationId, String receiver, SimpMessagingTemplate template) {
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

    @Override
    public void privateMessage(OutgoingMessage outgoingMessage, SimpMessagingTemplate template) {
        if (outgoingMessage.getContent() == null) {
            return;
        }

        System.out.println("Outgoing message: " + outgoingMessage);
        Conversation conversation = conversationDao.findById(outgoingMessage.getConversationId()).get();

       /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();*/
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
}
