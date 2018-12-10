package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.IncomingMessage;
import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MessageController {
    @Autowired
    UserDao userDao;
    @Autowired
    MessageDAO messageDAO;

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
    @PostMapping("/createConversationOrMessage")
    public String createConversationOrMessage(
            @RequestBody Map<String, Long> friendId
    ){
        Long id = friendId.get("friendId");
        System.out.println(id);


        return "redirect:/friends";
    }
}
