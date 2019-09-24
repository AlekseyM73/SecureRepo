package com.alekseymakarov.locknote.utils;

import java.util.Calendar;
import java.util.Date;

public class DateConverter {

    public static String convertDate (long time){
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (Integer.valueOf(month) < 10){
            month = "0"+month;
        }
        if (Integer.valueOf(day) < 10){
            day = "0"+day;
        }

        return day + "." + month + "." + year;
    }
}
