package com.owu.geekhub.service.validation;

import java.sql.Date;
import java.util.Calendar;

public class DateValidator {
    public boolean isDateVALID(Date date){
        if(date == null){
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.setTime(date);
        try {
            cal.getTime();

        }
        catch (Exception e) {
            System.out.println("Invalid date");
            return false;
        }
        System.out.println("Date is valid!!!");
        return true;
    }
}
