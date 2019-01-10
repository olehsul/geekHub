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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    // mapping for url '/messages'
    @GetMapping("/messages")
    public String messages(@RequestParam Long interlocutorId, Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (interlocutorId != null) {
            List<UserConversation> userConversations = userConversationDao.findAllByUser_id(interlocutorId);
            for (UserConversation userConversation : userConversations) {
                Conversation conversation = conversationDao.findById(userConversation.getConversation_id()).get();
                List<User> users = conversation.getUsers();

                if (users.get(0).getId().equals(principal.getId()))
                    model.addAttribute("interlocutor", users.get(1));
                else if (users.get(1).getId().equals(principal.getId()))
                    model.addAttribute("interlocutor", users.get(0));
            }
        }
        model.addAttribute("loggedUser", principal);
        return "user/messages";
    }

    //test mapping for messaging system
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

    // mapping for request to load conversation lost for user
    @MessageMapping("/conversations-request-for-id{userId}")
    @SendTo("/topic/conversations-list-for-id{userId}")
    public List<Conversation> getConversations(@DestinationVariable Long userId) {
//        System.out.println("INSIDE CONVERSATION MESSAGE MAPPING");
        User user = userDao.findById(userId).get();
        System.out.println(user);
        return user.getConversations();
    }

    // mapping for request to load messages for selected conversation
    @MessageMapping("/messages-for-conversation-id{conversationId}")
    @SendTo("/topic/messages-list-for-conversation-id{conversationId}")
    public List<Message> getMessages(@DestinationVariable Long conversationId) {
//        System.out.println("INSIDE GET-MESSAGES______________________________________");
        List<Message> list = messageDAO.findAllByConversation_Id(conversationId);
        for (Message message : list) {
            System.out.println(message);
        }
        return list;
    }

    // mapping for sent message to show both users & update conversations list
    @MessageMapping("/private-message")
    public void privateMessage(IncomingMessage incomingMessage) {
//        System.out.println("INSIDE SEND PRICATE MSG_________________________________");
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

        template.convertAndSend("/topic/conversation-for-id" + incomingMessage.getSenderId(), conversationDao.findById(incomingMessage.getConversationId()).get());
        template.convertAndSend("/topic/conversation-for-id" + incomingMessage.getRecipientId(), conversationDao.findById(incomingMessage.getConversationId()).get());
        template.convertAndSend("/topic/messages-list-for-conversation-id" + incomingMessage.getConversationId(), messageDAO.findAllByConversation_Id(incomingMessage.getConversationId()));

//        sendMsg("topic/conversation-for-id" + incomingMessage.getSenderId(), conversationDao.findById(incomingMessage.getConversationId()).get());
//        sendMsg("topic/conversation-for-id" + incomingMessage.getRecipientId(), conversationDao.findById(incomingMessage.getConversationId()).get());
//        sendMsg("topic/messages-list-for-conversation-id" + incomingMessage.getConversationId(), messageDAO.findAllByConversation_Id(incomingMessage.getConversationId()));

    }

      // custom sendTo method (doesnt work)
//    @MessageMapping("/")
//    @SendTo("/{url}")
//    private Object sendMsg(@DestinationVariable("url") String url, Object data) {
////        System.out.println("INSIDE SENDTO METHOD____________________________________________");
//        return data;
//    }

    @PostMapping("/createConversationOrMessage")
    public String createConversationOrMessage(
            @RequestBody Map<String, Long> friendId
    ) {
        Long id = friendId.get("friendId");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        messageService.createConversationIfNotExists(principal.getId(), id);

        return "redirect:/friends?interlocutorId=" + id;
    }

}
