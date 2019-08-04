package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
public class ApiUserRestController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserFriendService userFriendService;

    @Value("${upload.path:#{null}}")
    private String uploadPath;
    @GetMapping("/api/auth-user/")
    public User getUser(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         return (User) authentication.getPrincipal();
    }

    @GetMapping("/api/user/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userDao.findById(userId).get();
    }

    @GetMapping("/api/get-user-by-username")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PM')")
    public User getUserByUsername(@RequestParam String username) {
        return userDao.findByUsername(username);
    }

    @GetMapping("/api/user-friends")
    public List<User> getFriendsByUsername(@RequestParam String username) {
        return userDao.findByUsername(username).getFriends();
    }

    @GetMapping("/api/friends")
    public List<User> getFriendsList() {
        log.debug("Getting friends list...");
        return userFriendService.getFriendsList();
    }

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
}
