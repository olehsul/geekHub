package com.owu.geekhub.service;

import com.owu.geekhub.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
}
