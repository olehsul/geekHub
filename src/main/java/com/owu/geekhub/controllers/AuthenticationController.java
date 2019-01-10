package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.security.jwt.JwtProvider;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;


@Controller
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    private UserDao userDao;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;


    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @PostMapping("/registerNewUser")
    public String registerNewUser(
            User user,
            @RequestParam("birth-date") Date birthDate,
            HttpServletRequest request
    ) throws MessagingException {
        user.setBirthDate(birthDate);
        System.out.println(user);

        if (userService.save(user)) {
//            authWithHttpServletRequest(request, user.getUsername(), password);
            mailService.sendActivationKey(user.getUsername());
            System.out.println("USER SAVED SUCCESSFULLY");
            return "redirect:/verification-request/id" + user.getId() + "";
        } else {
            System.out.println("WRONG DATA ENTERED");
            return "redirect:/auth";
        }

    }

    @GetMapping("/auth")
    public String auth() {
        System.out.println("---------you are in auth method-----------");
        if (!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        } else {
            return "authentication/auth";
        }
    }

    @GetMapping("/login")
    public String login() {
        if (!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        } else {
            return "authentication/login";
        }
    }

    @PostMapping("/success-login")
    public String successURL(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        if (principal.getUsername().equals("admin"))
            return "redirect:/admin";
        User user = (User) authentication.getPrincipal();
        if (!user.isActivated()) {
            // log out user until email confirmation
            new SecurityContextLogoutHandler().logout(request, null, null);
            return "redirect:/verification-request/id" + user.getId();
        }
        return "redirect:/id" + user.getId();
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
        mailService.sendActivationKey(user.getUsername());
        model.addAttribute("userId", id);
        return "redirect:/verification-request/id" + user.getId();
    }

    @GetMapping("/verification-request/id{id}")
    public String verification(@PathVariable Long id,
                               Model model) throws MessagingException {
        User user = userDao.findById(id).get();
//        mailService.send(user.getUsername());
        model.addAttribute("userId", id);
        return "authentication/verification";
    }

    @PostMapping("/verify/id{id}")
    public String verify(@PathVariable Long id, @RequestParam int activationKey) {
        User user = userDao.findById(id).get();
        System.out.println(user);
        if (user.getActivationKey() == activationKey) {
            user.setActivated(true);
            userService.update(user);
            return "authentication/login";
        } else return "redirect:/verification-request/id" + id;
    }
//    @GetMapping("/emailRecovery")
//    public String emailRecovery(){
////        System.out.println("================you are in emailrecovery================");
//        return "authentication/recovery-email";
//    }

    @PostMapping("/passwordRecovery")
    public String passwordRecovery(
            @RequestParam String email, Model model) throws MessagingException {
        if (userDao.existsDistinctByUsername(email)) {
            mailService.sendRecoveryCode(email);
            model.addAttribute("email", email);

            return "authentication/recovery-password";
        }
        return "authentication/login";
    }

    @PostMapping("/setNewPassword")
    public String setNewPassword(@RequestParam String activationKey,
                                 @RequestParam String email,
                                 @RequestParam String password) {
        int key = Integer.parseInt(activationKey);
        User user = userDao.findByUsername(email);
        if (user.getActivationKey() == key) System.out.println("=======user.getActivationKey() == key=========k");
        if ((user.getActivationKey() == key)&&(userService.validatePassword(password))){
            System.out.println("=====password could be changed===========");
            user.setPassword(password);
            userService.updatePassword(user);
        }else System.out.println("-======something is wrong, password was not updated=======");

        return "authentication/login";
    }


}
