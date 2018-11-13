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

    @Autowired
    private RandomUserIdentity randomUserIdentity;

    @Autowired
    private UserIdentityDao userIdentityDao;

    @Autowired
    private RandomVerificationNumber randomVerificationNumber;

    @Autowired
    private MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUsername(s);
        return user;
    }


    @Override
    public boolean save(User user) throws MessagingException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        String date = format.format(user.getBirthDate());

        if (
                !registrationValidator.isNameValid(user.getFirstName())
                        || !registrationValidator.isDateValid(date)
                        || !registrationValidator.isPasswordValid(user.getPassword())
        ) {
            System.out.println("-----some registration data are wrong---------");
            return false;
        }
        System.out.println("first name valid = " + registrationValidator.isNameValid(user.getFirstName()));
        System.out.println("last name valid = " + registrationValidator.isNameValid(user.getLastName()));
        System.out.println("password valid = " + registrationValidator.isPasswordValid(user.getPassword()));
        System.out.println("date valid = " + registrationValidator.isDateValid(date));

        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);

        int verificationNumber = randomVerificationNumber.getRandomVerifictionNumber();
        user.setActivationKey(verificationNumber);
        mailService.send(user.getUsername(), Integer.toString(verificationNumber));

//
//        long randomUserIdentity;
//        boolean identityAlreadyExist = false;
//        do{
//            randomUserIdentity = this.randomUserIdentity.createRandomUserIdentity();
//
//            List<UserIdentity> userIdentities = userIdentityDao.findAll();
//            Iterator<UserIdentity> iterator = userIdentities.iterator();
//
//            while (iterator.hasNext()){
//                UserIdentity identity = iterator.next();
//                if (identity.getUserId().equals(randomUserIdentity)){
//                    System.out.println("-------------identity matches!!----------");
//                    identityAlreadyExist = true;
//                }
//            }
//        }while (identityAlreadyExist);
//        System.out.println("================" + randomUserIdentity);
//        UserIdentity userIdentity = new UserIdentity();
//        userIdentity.setUserId(randomUserIdentity);
//        user.setIdentity(userIdentity);
//        userIdentity.setUser(user);


        user.setEnabled(false);
        user.setRole(Role.ROLE_USER);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setActivated(false);

        userDao.save(user);
        System.out.println("----------Registration data is Ok");
        return true;
    }

    @Override
    public void update(User user){
        userDao.save(user);
    }


}
