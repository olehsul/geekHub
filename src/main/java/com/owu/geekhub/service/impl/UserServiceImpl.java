package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.dao.UserIdentityDao;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserIdentity;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.generators.RandomUserIdentity;
import com.owu.geekhub.service.generators.RandomVerificationNumber;
import com.owu.geekhub.service.validation.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RegistrationValidator registrationValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private RandomVerificationNumber randomVerificationNumber;
//
//    @Autowired
//    private MailService mailService;

    @Autowired
    private RandomUserIdentity randomUserIdentity;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUsername(s);
        return user;
    }

    private void encodePassword(User user){
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
    }

    private boolean isUserAlreadyRegistered(User user){
        if (userDao.existsDistinctByUsername(user.getUsername())){
            System.out.println("========user " + user.getUsername()+ " already exist=========");
            return true;
        }
        return false;
    }


    @Override
    public boolean save(User user){

        if (!registrationValidator.validateRegistrationData(user)) return false;
        if (isUserAlreadyRegistered(user)) return false;
        randomUserIdentity.setRandomId(user);
        encodePassword(user);
        System.out.println(user);

        user.setEnabled(true);
        user.setRole(Role.ROLE_USER);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        userDao.save(user);
        System.out.println("User is saved");
        return true;
    }

    @Override
    public void update(User user){
        encodePassword(user);
        userDao.save(user);
    }


}
