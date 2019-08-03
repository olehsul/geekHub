package com.owu.geekhub.service.validation;

import com.owu.geekhub.models.User;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class RegistrationValidator {
    public boolean isNameValid(String name){
        String cyrillicPattern = "[А-ЯЄІЇ][а-яєіїА-ЯЄІЇ]*";
        String latinPattern = "[A-Z][a-zA-Z]*";
        return (
                (name.matches( cyrillicPattern) ||(name.matches(latinPattern)))
                        &&
                (name.length() < 40)
        );
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
            System.out.println("=========date are invalid========");
            return false;
        }
    }

    public boolean isPasswordValid(String password){
        String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        boolean bool = password.matches(passwordPattern);
        System.out.println("==========is password valid " + bool);
        return bool;
    }

    public boolean validateRegistrationData(User user){
        if (!isNameValid(user.getFirstName())
                        || !isPasswordValid(user.getPassword())
        ) {
            System.out.println("-----some registration data are wrong--------");
            return false;
        }
        return true;
    }
}
