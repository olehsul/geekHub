package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.dao.UserFriendDAO;
import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserFriend;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserFriendsController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFriendDAO userFriendDAO;
    @Autowired
    private UserDao userDao;

    @GetMapping("/friends")
    public String friends(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        List<UserFriend> friends = userFriendDAO.findAllByFriendIdAndStatus(principal.getId(), FriendStatus.PENDING);

        List<User> users = new ArrayList<>();

        for (UserFriend friend : friends) {
            System.out.println("_________________________________________");
            System.out.println(friend);
            System.out.println("_________________________________________");
            users.add(userDao.findById(friend.getUserId()).get());
        }
        for (User user : users) {
            System.out.println("++++++" + user);
        }
        model.addAttribute("friends", users);
        return "user/friends-list";
    }

    @GetMapping("/{userID}/friends")
    public String userFriends() {

        return "";
    }


}
