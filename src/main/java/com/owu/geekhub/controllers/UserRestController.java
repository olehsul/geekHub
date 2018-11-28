package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserRestController {

    @Autowired
    private UserDao userDao;

    @PostMapping("/findUser")
    public void findUser(

            @RequestBody Map<String, String> fullName
    ) {
        String firstName = fullName.get("firstName") + "";
        String lastName = fullName.get("lastName") + "";
        System.out.println(firstName + " " + lastName);
        List<User> users = userDao.findAllByFirstNameContainsAndLastNameContains(firstName, lastName);
        for (User user : users) {
            System.out.println(user);
        }
    }

}
