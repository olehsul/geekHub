package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Gender;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;


    @GetMapping("/id{userId}")
    public String userId(@PathVariable Long userId,
                         Model model) {
        User friend = userDao.findById(userId).get();
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("=========going home user" + principal.getUsername());
        User user = userDao.findById(principal.getId()).get();

        model.addAttribute("loggedUser", principal);
        model.addAttribute("userPage", friend);

        boolean areUserFriends = false;
        List<User> userFriends = user.getFriends();
        for (User userFriend : userFriends) {
            if (userFriend.getId().equals(friend.getId())){
                areUserFriends = true;
                break;
            }
        }
        System.out.println("are users friends " + areUserFriends);
        model.addAttribute("areFriends", areUserFriends);

        System.out.println(friend);
        return "user/home";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        User user = User.builder()
                .id(new Long("0"))
                .username("admin")
                .password("admin")
                .firstName("ADMIN")
                .lastName("ACCOUNT")
                .gender(Gender.MALE)
                .build();
        model.addAttribute("user", user);
        return "user/home";
    }
}
