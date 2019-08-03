package com.owu.geekhub.service;

import com.owu.geekhub.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    ResponseEntity<?> save(User user) throws MessagingException;

    void update(User user);

    void updatePassword(User user);

    List<User> searchUser(String name, String surname);
}
