package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
public class UserSearchRestController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/api/find-user")
    public List<UserSearchModel> findUser(
            @RequestParam String fullName
    ) {
        System.out.println(fullName);
        String[] splitFullName = fullName.split(" ");
        System.out.println(splitFullName[0]);
        if (splitFullName.length > 1){
            System.out.println(splitFullName[1]);
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        String firstName = splitFullName[0];
        String lastName = "";
        if (splitFullName.length > 1){
            lastName = splitFullName[1];
        }
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
        System.out.println(foundUsers);

        return foundUsers;
    }


}
