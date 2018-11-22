package com.owu.geekhub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FriendsController {
    @GetMapping("/friend-request-{friendId}-from-{userId}")
    public String friendRequest(
            @PathVariable Long friendId,
            @PathVariable Long userId
    ) {

        return "";
    }
    
}
