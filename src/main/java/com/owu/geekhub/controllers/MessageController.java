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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
        return messageService.getConversations(username);
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
        messageService.privateMessage(outgoingMessage, template);
    }

    @MessageMapping("/set-messages-as-read-in-conversation-{conversationId}-for-{receiver}")
    public void saveMessageAsRead(
            @DestinationVariable Long conversationId,
            @DestinationVariable String receiver
//            @DestinationVariable String sender
    ) {
        messageService.saveMessageAsRead(conversationId, receiver, template);
    }

    @GetMapping("/api/unread-messages")
    public List<Message> getUnreadMessages(@RequestParam String username) {
        return this.userDao.findByUsername(username).getUnreadMessages();
    }

    @PostMapping("/goto-conversation")
    public Conversation createConversationIfNotExistsAndGet(
            @RequestParam Long friendId
    ) {
        return messageService.gotoConversation(friendId);
    }
}

