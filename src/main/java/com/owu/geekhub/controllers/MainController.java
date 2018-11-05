package com.owu.geekhub.controllers;

import com.owu.geekhub.models.Contact;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {



    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("message", "register");
        return "index";
    }

    @GetMapping("/save")
    public String save(
            @RequestParam ("email") String email,
            @RequestParam ("password") String password

    ){
        Contact contact = new Contact (email, password);
        return "index";
    }



}
