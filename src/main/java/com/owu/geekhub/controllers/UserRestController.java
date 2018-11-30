package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestController {

    @Autowired
    private UserDao userDao;

    @PostMapping("/findUser")
    public List<UserSearchModel> findUser(

            @RequestBody Map<String, String> fullName
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        String firstName = fullName.get("firstName") + "";
        String lastName = fullName.get("lastName") + "";
        List<User> users = new ArrayList<>();
        if (!firstName.equals("")){
            users = userDao.findAllByFirstNameContainsAndLastNameContains(firstName, lastName);
        }

        List<UserSearchModel> foundUsers = new ArrayList<>();
        for (User user : users) {
            System.out.println(user);
            if (user.getId().equals(principal.getId())) continue;
            foundUsers.add(new UserSearchModel(user.getId(), user.getFirstName(), user.getLastName()));
        }

        return foundUsers;
    }

}
