package com.owu.geekhub.controllers;

import com.owu.geekhub.models.Conversation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @GetMapping("/ping")
    public String getConversations() {
        return "pong";
    }

}
