package com.owu.geekhub.controllers;

import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserFriendsController{
    @Autowired
    UserService userService;
    
    @GetMapping("/friends")
    public String friends() {
        
        return "friends";
    }
    
    @GetMapping("/{userID}/friends")
    public String userFriends() {
        
        return "";
    }
    
    
}
