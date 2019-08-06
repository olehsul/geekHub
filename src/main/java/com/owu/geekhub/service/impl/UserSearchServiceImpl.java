package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserSearchModel;
import com.owu.geekhub.service.UserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserSearchServiceImpl implements UserSearchService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<UserSearchModel> findUser(String fullName) {
        log.debug("Searching user with full name: " + fullName);
        String[] splitFullName = fullName.split(" ");
//        System.out.println(splitFullName[0]);
//        if (splitFullName.length > 1){
//            System.out.println(splitFullName[1]);
//        }


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
            log.debug("Found user: " + user);
            if (user.getId().equals(principal.getId())) continue;
            foundUsers.add(new UserSearchModel(user.getId(), user.getFirstName(), user.getLastName()));
        }
        return foundUsers;
    }
}
