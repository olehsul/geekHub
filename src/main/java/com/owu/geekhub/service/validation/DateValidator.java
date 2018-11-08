package com.owu.geekhub.service.validation;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class DateValidator {
    public boolean isDateValid(String date){
        if(date == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException ex) {
            return false;
        }


    }
}
