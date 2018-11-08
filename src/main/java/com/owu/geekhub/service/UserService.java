package com.owu.geekhub.service;

import com.owu.geekhub.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    boolean save(User user);
}
