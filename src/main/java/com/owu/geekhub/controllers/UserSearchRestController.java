package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserSearchModel;
import com.owu.geekhub.service.UserSearchService;
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
    private UserSearchService userSearchService;

    @GetMapping("/api/find-user")
    public List<UserSearchModel> findUser(
            @RequestParam String fullName
    ) {
        return userSearchService.findUser(fullName);
    }


}
