package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.IncomingMessage;
import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import sun.awt.AWTAutoShutdown;

import java.sql.Date;
import java.util.Optional;

@Controller
public class MessageController {
    @Autowired
    UserDao userDao;
    @Autowired
    MessageDAO messageDAO;

    @MessageMapping("/roomName")
    @SendTo("/channelName/answer") // uncomment when build subscribe
    public Message answer(IncomingMessage incomingMessage) {
        System.out.println(incomingMessage);
        Message message = Message.builder()
                .content(incomingMessage.getContent())
                .build();
        message.setCreateDate(new Date(System.currentTimeMillis()));
        User sender = userDao.findById(incomingMessage.getSenderId()).get();
        message.setSender(sender);
        System.out.println("Sender: " + sender);
        System.out.println("message: " + message);
        messageDAO.save(message);
//        return new Date(System.currentTimeMillis()) + ": " + message.getContent();
        return message;
    }
}
