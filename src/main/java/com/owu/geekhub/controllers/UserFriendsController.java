package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.dao.UserFriendDao;
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
    private UserFriendDao userFriendDao;
    @Autowired
    private UserDao userDao;

    @GetMapping("/friends")
    public String friends(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        List<UserFriend> friendRequests = userFriendDao.findAllByFriendIdAndStatus(principal.getId(), (FriendStatus.PENDING));
        List<UserFriend> friends = userFriendDao.findAllByFriendIdAndStatus(principal.getId(), FriendStatus.APPROVED);
        List<User> usersRequests = new ArrayList<>();
        List<User> usersFriends = new ArrayList<>();

        for (UserFriend friend : friendRequests) {
            System.out.println("_________________________________________");
            System.out.println(friend);
            System.out.println("_________________________________________");
            usersRequests.add(userDao.findById(friend.getUserId()).get());
        }


        System.out.println(friends.size() + "size-----------");
        for (UserFriend friend : friends) {
            System.out.println(friend);
            System.out.println("_________________________________________");
            usersFriends.add(userDao.findById(friend.getUserId()).get());
        }

        for (User user : usersRequests) {
            System.out.println("++++++" + user);
        }

        for (User user : usersFriends) {
            System.out.println("------" + user);


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
