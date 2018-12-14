package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.util.List;

@Controller
public class UserFriendsController {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private UserDao userDao;

    @GetMapping("/friends")
    public String friends(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User user = userDao.findById(principal.getId()).get();
        List<User> usersRequests = user.getIncomingFriendShipRequests();
        List<User> usersFriends = user.getFriends();

        for (User user1 : usersRequests) {
            System.out.println("++++++" + user1);
        }

        for (User user1 : usersFriends) {
            System.out.println("------" + user1);


        }
        model.addAttribute("loggedUser", principal);
        model.addAttribute("friendsRequests", usersRequests);
        model.addAttribute("friends", usersFriends);
        return "user/friends-list";
    }

    @GetMapping("/{userID}/friends")
    public String userFriends() {

        return "";
    }


}
