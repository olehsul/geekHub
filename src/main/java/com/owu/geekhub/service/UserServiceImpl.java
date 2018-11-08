package com.owu.geekhub.service;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.validation.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RegistrationValidator registrationValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUsername(s);
        return user;
    }


    @Override
    public boolean save(User user) {
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

        user.setRole(Role.ROLE_USER);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setActive(true);

        userDao.save(user);
        System.out.println("----------Registration data is Ok");
        return true;
    }


}
