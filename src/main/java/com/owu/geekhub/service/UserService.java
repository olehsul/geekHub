package com.owu.geekhub.service;

import com.owu.geekhub.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface UserService extends UserDetailsService {
    boolean save(User user) throws MessagingException;
    void update(User user);
    void updatePassword(User user);
    boolean validatePassword(String password);
}
