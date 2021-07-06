package com.owu.geekhub.util;

import com.owu.geekhub.aspect.Validator;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.exception.GenericGeekhubException;
import com.owu.geekhub.exception.UserInvalidException;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class UserRegistrationValidator implements Validator {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public <U extends GenericGeekhubException> void validate(Object target) throws UserInvalidException {

        User user = (User) target;
        boolean isValid = true;
        UserInvalidException exception = new UserInvalidException("User data are invalid");
        if (user.getBirthDate().after(Date.valueOf(java.time.LocalDate.now()))) {
            exception.getErrors().add("User born in future can not be signed up... ¯\\_(ツ)_/¯");
            isValid = false;
        }
        if (userDao.existsDistinctByUsername(user.getUsername())) {
            exception.getErrors().add("User with such email already exists!");
            isValid = false;
        }
        if (!isNameValid(user.getFirstName()) || !isNameValid(user.getLastName())) {
            exception.getErrors().add("First or Last name is invalid");
            isValid = false;
        }
        if (!isPasswordValid(user.getPassword())) {
            exception.getErrors().add("User's password doesn't match the requirements!");
            isValid = false;
        }

        if (!isValid) throw exception;

    }

    private boolean isNameValid(String name){
        String cyrillicPattern = "[А-ЯЄІЇ][а-яєіїА-ЯЄІЇ]*";
        String latinPattern = "[A-Z][a-zA-Z]*";
        return (
                (name.matches( cyrillicPattern) ||(name.matches(latinPattern)))
                        &&
                        (name.length() < 40)
        );
    }

    private boolean isPasswordValid(String password){
        String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        boolean bool = password.matches(passwordPattern);
        System.out.println("==========is password valid " + bool);
        return bool;
    }

    public boolean isDateValid(String date){
        if(date == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(date);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException ex) {
            System.out.println("=========the date is invalid========");
            return false;
        }
    }

}
