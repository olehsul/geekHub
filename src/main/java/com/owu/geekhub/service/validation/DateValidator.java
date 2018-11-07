package com.owu.geekhub.service.validation;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateValidator {
    public boolean isDateValid(String date){
        if(date == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException ex) {
            return false;
        }


    }
}
