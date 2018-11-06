package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Contact;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.ws.Action;

@Controller
public class MainController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello");

        return "index";
    }

    @PostMapping ("/successURL")
    public String successURL(){
        System.out.println("successURL______________________________________");
        return "redirect:/";
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/registerNewUser")
    public String registerNewUser(
            User user
    ){
        System.out.println(user);
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        System.out.println(user);
        userService.save(user);

        return "redirect:/";
    }



}
