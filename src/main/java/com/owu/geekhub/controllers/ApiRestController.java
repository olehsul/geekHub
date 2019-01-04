package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
public class ApiRestController {
    @Autowired
    private UserDao userDao;

    @GetMapping("/api/users")
    public List<User> allUsers() {

        return userDao.findAll();
    }
}
