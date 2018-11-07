package com.owu.geekhub.service;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Contact;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.validation.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DateValidator dateValidator;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.findByUsername(s);
    }

    @Override
    public void save(User user) {
        if (user != null) {
//            if (!dateValidator.isDateValid(user.getBirthDate()))
//                return;
            user.setRole(Role.ROLE_USER);
            user.setAccountNonExpired(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNonLocked(true);
            user.setEnabled(true);
            user.setActive(true);

            userDao.save(user);
        }
    }
}
