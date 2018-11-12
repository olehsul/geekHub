package com.owu.geekhub.controllers;

import com.owu.geekhub.models.User;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.generators.RandomVerificationNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private RandomVerificationNumber verificationNumber;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @PostMapping("/registerNewUser")
    public String registerNewUser(
            User user,
            @RequestParam("birth-date") String birthDate,
            HttpServletRequest request
    ) throws MessagingException {
        String password = user.getPassword();
//        System.out.println(user);
        String datePattern = "dd/MM/yyyy";
        try {
            user.setBirthDate(new Date(new SimpleDateFormat(datePattern).parse(birthDate).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("-------------im in saver----------------");
        if (userService.save(user)) {
//            authWithHttpServletRequest(request, user.getUsername(), password);
            int randomVerifictionNumber = verificationNumber.getRandomVerifictionNumber();

            return "redirect:/id" + user.getId();
        } else return "redirect:/auth";

    }

    @GetMapping("/auth")
    public String auth() {
        if (!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        } else {
            return "auth";
        }
    }

    @PostMapping("/successURL")
    public String successURL() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "redirect:/id" + ((User) authentication.getPrincipal()).getId();
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes()).getRequest();
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.err.println("Login Error!");
            e.printStackTrace();
//            LOGGER.error("Error while login ", e);
        }
    }

    @PostMapping("/verify")
    public String verify(int activationKey) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getActivationKey() == activationKey) {
            user.setEnabled(true);
            return "/auth";
        } else return "/verification";
    }

}
