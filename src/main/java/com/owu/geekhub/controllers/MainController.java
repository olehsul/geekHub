package com.owu.geekhub.controllers;

import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class MainController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello");

        return "index";
    }
}
