package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.jwtmessage.response.ResponseMessage;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.generators.RandomUserIdentity;
import com.owu.geekhub.service.validation.RegistrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final MailService mailService;

    private final UserDao userDao;

    private final RegistrationValidator registrationValidator;

    private final PasswordEncoder passwordEncoder;

    private final RandomUserIdentity randomUserIdentity;

    public UserServiceImpl(MailService mailService, UserDao userDao, RegistrationValidator registrationValidator, PasswordEncoder passwordEncoder, RandomUserIdentity randomUserIdentity) {
        this.mailService = mailService;
        this.userDao = userDao;
        this.registrationValidator = registrationValidator;
        this.passwordEncoder = passwordEncoder;
        this.randomUserIdentity = randomUserIdentity;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.findByUsername(s);
    }

    private void encodePassword(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
    }

    private boolean isUserAlreadyRegistered(User user) {
        return userDao.existsDistinctByUsername(user.getUsername());
    }


    @Override
    public synchronized ResponseEntity<?> save(User user) {
        if (user.getBirthDate().after(Date.valueOf(java.time.LocalDate.now()))) {
            return new ResponseEntity<>(new ResponseMessage("User born in future can not be signed up... ¯\\_(ツ)_/¯"),
                    HttpStatus.BAD_REQUEST);
        } else if (isUserAlreadyRegistered(user)) {
            return new ResponseEntity<>(new ResponseMessage("User with such email already exists!"),
                    HttpStatus.BAD_REQUEST);
        } else if (!registrationValidator.validateRegistrationData(user)) {
            return new ResponseEntity<>(new ResponseMessage("Invalid credentials!"),
                    HttpStatus.BAD_REQUEST);
        }
        completeUser(user);
        userDao.save(user);
        try {
            mailService.sendActivationKey(user.getUsername());
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e.getCause());
            return new ResponseEntity<>(new ResponseMessage("There was an error sending the message"),
                    HttpStatus.BAD_REQUEST);
        }
        logger.info("New User registered: " + user);
        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    private void completeUser(User user) {
        randomUserIdentity.setRandomId(user);
        encodePassword(user);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setRole(Role.ROLE_USER);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setProfileImage("general.png");
    }

    @Override
    public List<User> searchUser(String name, String surname) {
        return userDao.findAllByFirstNameContainsAndLastNameContains(name, surname);
    }

    @Override
    public void update(User user) {
        userDao.save(user);
    }

    @Override
    public void updatePassword(User user) {
        encodePassword(user);
        userDao.save(user);
    }


}
