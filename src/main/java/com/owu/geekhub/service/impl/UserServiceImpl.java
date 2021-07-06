package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.exception.GenericGeekhubException;
import com.owu.geekhub.exception.UserInvalidException;
import com.owu.geekhub.jwtmessage.response.ResponseMessage;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserActivationService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.util.RandomUserIdentity;
import com.owu.geekhub.util.UserRegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserActivationService userActivationService;
    private final PasswordEncoder passwordEncoder;
    private final RandomUserIdentity randomUserIdentity;
    private final UserRegistrationValidator validator;

    @Autowired
    public UserServiceImpl(
            UserDao userDao,
            UserActivationService userActivationService,
            PasswordEncoder passwordEncoder,
            RandomUserIdentity randomUserIdentity,
            UserRegistrationValidator validator) {
        this.userDao = userDao;
        this.userActivationService = userActivationService;
        this.passwordEncoder = passwordEncoder;
        this.randomUserIdentity = randomUserIdentity;
        this.validator = validator;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        return userDao.findByUsername(s);
    }

    private void encodePassword(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
    }



    @Override
    public synchronized ResponseEntity<?> save(User user) {

        if (validator.supports(User.class)){
            try {
                validator.validate(user);
            } catch (UserInvalidException e) {
                return new ResponseEntity<>(new ResponseMessage(StringUtils.join(e.getErrors(), ", \n")),
                        HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseMessage(e.getMessage()),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        completeUser(user);
        userDao.save(user);
        ResponseEntity<?> responseEntity = sendNewActivationCode(user.getUsername());
//        try {
//            userActivationService.sendTemporaryCode(user);
//        } catch (GenericGeekhubException e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(new ResponseMessage(e.getMessage()),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        if (responseEntity.equals(ResponseEntity.ok().build())) {
            log.debug("New User registered: " + user);
            MultiValueMap<String, String> httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json");
            return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), httpHeaders, HttpStatus.OK);
        } else return responseEntity;

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
        user.setActivationKey("00000");
    }

    @Override
    public List<User> searchUser(String name, String surname) {
        return userDao.findAllByFirstNameContainsAndLastNameContains(name, surname);
    }

    @Override
    public ResponseEntity<?> activateUser(String username, String code) {
        User user = userDao.findByUsername(username);
        if (user == null)
            return ResponseEntity.badRequest().body(new ResponseMessage("User " + username + " not found"));

        if (userActivationService.verifyCodeAndActivateUser(user, code))
            return ResponseEntity.ok(new ResponseMessage("User was activated successfully"));
        else return ResponseEntity.badRequest().body(new ResponseMessage("The activation code was wrong"));
    }

    @Override
    public ResponseEntity<?> sendNewActivationCode(String username) {
        User user = userDao.findByUsername(username);
        if (user == null)
            return ResponseEntity.badRequest().body(new ResponseMessage("User " + username + " not found"));
        try {
            userActivationService.sendTemporaryCode(user);
        } catch (GenericGeekhubException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
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
