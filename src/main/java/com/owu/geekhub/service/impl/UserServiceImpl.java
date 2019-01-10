package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserPrinciple;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.generators.RandomUserIdentity;
import com.owu.geekhub.service.validation.RegistrationValidator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username)
//                .orElseThrow(
//                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username))
                ;

        return UserPrinciple.build(user);
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
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        userDao.save(user);
        System.out.println("User is saved");
        return true;
    }

    public boolean validatePassword(String password){
        if (registrationValidator.isPasswordValid(password)){
            return true;
        }
        return false;
    }

    @Override
    public List<User> searchUser(String name, String surname) {
        return userDao.findAllByFirstNameContainsAndLastNameContains(name, surname);
    }

    @Override
    public void update(User user){
        userDao.save(user);
    }

    @Override
    public void updatePassword(User user){
        encodePassword(user);
        userDao.save(user);
    }


}
