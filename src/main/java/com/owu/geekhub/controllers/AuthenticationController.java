package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RandomVerificationNumber randomVerificationNumber;

    @Autowired
    private MailService mailService;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @PostMapping("/registerNewUser")
    public String registerNewUser(
            User user,
            @RequestParam("birth-date") String birthDate,
            HttpServletRequest request
    ) throws MessagingException {
        String password = user.getPassword();
        String datePattern = "dd/MM/yyyy";
        try {
            user.setBirthDate(new Date(new SimpleDateFormat(datePattern).parse(birthDate).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (userService.save(user)) {
//            authWithHttpServletRequest(request, user.getUsername(), password);
            System.out.println("USER SAVED SUCCESSFULLY");
            return "redirect:/verification-request/id"+ user.getId() +"";
        } else {
            System.out.println("WRONG DATA ENTERED");
            return "redirect:/auth";
        }

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

    @PostMapping("/verify/sendCodeAgainFor/id{id}")
    public String sendCodeAgain(@PathVariable Long id,
                                Model model) throws MessagingException {
        System.out.println("======send_new_code==================");
        User user = userDao.findById(id).get();
        mailService.send(user.getUsername());
        model.addAttribute("userId", id);
        return "verification";
    }

    @GetMapping("/verification-request/id{id}")
    public String verification(@PathVariable Long id,
                               Model model) throws MessagingException {
        User user = userDao.findById(id).get();
        mailService.send(user.getUsername());
        model.addAttribute("userId", id);
        return "verification";
    }

    @PostMapping("/verify/id{id}")
    public String verify(@PathVariable Long id, @RequestParam int activationKey) {
        User user = userDao.findById(id).get();
        System.out.println(user);
        if (user.getActivationKey() == activationKey) {
            user.setEnabled(true);
            userService.update(user);
            return "/auth";
        } else return "redirect:/verification-request/id" + id;
    }

}
