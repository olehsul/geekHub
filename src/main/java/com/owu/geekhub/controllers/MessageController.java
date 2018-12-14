package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserConversationDao;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.*;
import com.owu.geekhub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/messages")
    public String messages(Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("loggedUser", principal);
        return "user/messages";
    }

    @MessageMapping("/sender-id{senderId}/receiver-id{recipientId}")
    @SendTo({"/topic/msg-answer/id{recipientId}", "/topic/msg-answer/id{senderId}"})
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

//        Set<Message> messages = messageDAO.findMessagesBySenderIdAndRecipientId(sender.getId(), recipient.getId());
//        messages.addAll(messageDAO.findMessagesBySenderIdAndRecipientId(recipient.getId(), sender.getId()));

//        LinkedHashSet<Message> collected = messages.stream()
//                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//        return new Date(System.currentTimeMillis()) + ": " + message.getContent();
        return null;
    }

    @MessageMapping("/conversation-for-id{userId}")
    @SendTo("/topic/conversations-list-for-id{userId}")
    public List<Conversation> getConversations(@DestinationVariable Long userId) {
        System.out.println("INSIDE CONVERSATION MESSAGE MAPPING");
        User user = userDao.findById(userId).get();
        System.out.println(user);

        return   user.getConversations();
    }

    @PostMapping("/createConversationOrMessage")
    public String createConversationOrMessage(
            @RequestBody Map<String, Long> friendId
    ) {
        System.out.println("you are in createConversationOrMessage----------------");
        Long id = friendId.get("friendId");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User user = userDao.findById(principal.getId()).get();
        User friend = userDao.findById(id).get();
        messageService.createConversationIfNotExists(user.getId(), friend.getId());


//        System.out.println("Does conv exists " + conversationDao.existsDistinctByUsers(users));
        return "redirect:/friends";
    }

}
