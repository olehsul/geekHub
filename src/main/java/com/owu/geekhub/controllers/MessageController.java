package com.owu.geekhub.controllers;

import com.owu.geekhub.configs.WSConfig;
import com.owu.geekhub.dao.ConversationDao;
import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserConversationDao;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.*;
import com.owu.geekhub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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

    //test mapping for messaging system
    @MessageMapping("/sender-id{senderId}/receiver-id{recipientId}")
    @SendTo({"/chat/msg-answer/id{recipientId}", "/chat/msg-answer/id{senderId}"})
    public Set<Message> answer(IncomingMessage incomingMessage, @DestinationVariable Long senderId, @DestinationVariable Long recipientId) {
        System.out.println(incomingMessage);
        Message message = Message.builder()
                .content(incomingMessage.getContent())
                .build();
        message.setCreateDate(new Date(System.currentTimeMillis()));
        User sender = userDao.findById(senderId).get();
        User recipient = userDao.findById(recipientId).get();
        message.setSender(sender);
        //message.setRecipient(recipient);
        System.out.println("sender: " + sender);
        System.out.println("recipient: " + recipient);
        System.out.println("message: " + message);
        messageDAO.save(message);

        return null;
    }

    // mapping for request to load conversation lost for user
    @MessageMapping("/conversations-request-for-{username}")
    @SendTo("/chat/conversations-list-for-{username}")
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
    public void privateMessage(IncomingMessage incomingMessage) {
        System.out.println("INSIDE SEND PRIVATE MSG_________________________________");
        Conversation conversation = conversationDao.findById(incomingMessage.getConversationId()).get();
        Message message = Message.builder()
                .content(incomingMessage.getContent())
                .sender(userDao.findById(incomingMessage.getSenderId()).get())
                .createDate(new Date(System.currentTimeMillis()))
                .conversation(conversation)
                .parentMessage(conversation.getTheLastMessage())
                .build();
        messageDAO.save(message);

        conversation.setTheLastMessage(message);

        conversationDao.save(conversation);

        template.convertAndSend("/chat/conversation-for-id" + incomingMessage.getSenderId(), conversationDao.findById(incomingMessage.getConversationId()).get());
        template.convertAndSend("/chat/conversation-for-id" + incomingMessage.getRecipientId(), conversationDao.findById(incomingMessage.getConversationId()).get());
        template.convertAndSend("/chat/messages-list-for-conversation-id" + incomingMessage.getConversationId(), messageDAO.findAllByConversation_Id(incomingMessage.getConversationId()));

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/goto-conversation")
    @ResponseBody
    public String createConversationOrMessage(
            @RequestParam Long friendId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Boolean check = userConversationDao.checkIfExists(principal.getId(), friendId) > 1;
        System.out.println(check);
        messageService.createConversationIfNotExists(principal.getId(), friendId);

        return check.toString();
    }

}
