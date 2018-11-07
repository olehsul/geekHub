package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Contact;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.validation.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.ws.Action;
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

    @PostMapping ("/successURL")
    public String successURL(){
        System.out.println("successURL______________________________________");
        return "redirect:/";
    }


    @Autowired
    PasswordEncoder passwordEncoder;

    DateValidator dateValidator = new DateValidator();
    @DateTimeFormat (pattern="yyyy/MM/dd")
    @PostMapping("/registerNewUser")
    public String registerNewUser(
            User user,
            @RequestParam("birth-date") String birthDate
    ) {
        System.out.println(user);
        String datePattern = "dd/MM/yyyy";
        try {
            user.setBirthDate(new Date(new SimpleDateFormat(datePattern).parse(birthDate).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        System.out.println(user);
        userService.save(user);

        return "redirect:/";
    }



}
