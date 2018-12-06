package com.owu.geekhub.controllers;

import com.owu.geekhub.models.IncomingMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    @MessageMapping("/roomName")
//    @SendTo("channelName/answer") // uncomment when build subscribe
    public void answer(IncomingMessage incomingMessage) {
        System.out.println(incomingMessage);
    }
}
