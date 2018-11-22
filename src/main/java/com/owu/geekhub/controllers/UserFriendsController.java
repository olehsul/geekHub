package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserFriendDAO;
import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserFriendsController {
    @Autowired
    UserService userService;
    @Autowired
    UserFriendDAO userFriendDAO;

    @GetMapping("/friends")
    public String friends(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        List<User> friends = userFriendDAO.findAllByFriendIdAndStatus(principal.getId(), FriendStatus.PENDING);

        for (User friend : friends) {
            System.out.println("_________________________________________");
            System.out.println(friend);
        }
        model.addAttribute("friends", friends);
        return "user/friends-list";
    }

    @GetMapping("/{userID}/friends")
    public String userFriends() {

        return "";
    }


}
