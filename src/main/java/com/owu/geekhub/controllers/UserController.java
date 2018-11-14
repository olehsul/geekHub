package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Gender;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;

    @GetMapping("/id{userId}")
    public String userId(@PathVariable Long userId,
                         Model model) {
        User user = userDao.findById(userId).get();
        model.addAttribute("user", user);
        System.out.println(user);
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
                .role(Role.ROLE_ADMIN)
                .build();
        model.addAttribute("user", user);
        return "user/home";
    }
}
