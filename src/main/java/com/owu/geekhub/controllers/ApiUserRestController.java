package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
public class ApiUserRestController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserFriendService userFriendService;

    @GetMapping("/api/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PM')")
    public User getUserById(@PathVariable Long userId) {
        User user = userDao.findById(userId).get();
        System.out.println(user);
        return user;
    }

    @GetMapping("/api/get-user-by-username")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PM')")
    public User getUserByUsername(@RequestParam String username) {
        User user = userDao.findByUsername(username);
        System.out.println(user);
        return user;
    }

    @GetMapping("/api/user-friends")
    public List<User> getFriendsByUsername(@RequestParam String username) {
        return userDao.findByUsername(username).getFriends();
    }

    @GetMapping("/api/friends")
    public List<User> getFriendsList() {
        System.out.println("getting friends list");
        return userFriendService.getFriendsList();
    }

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
}
