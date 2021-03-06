package com.brduo.localee.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by anjoshigor on 10/07/17.
 */

public class StringsFormatter {

    public static String formatDate(Date date) {
        //date
        Calendar auxCalendar = new GregorianCalendar();
        auxCalendar.setTime(date);
        String month = auxCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        int day = auxCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = auxCalendar.get(Calendar.HOUR);
        int minute = auxCalendar.get(Calendar.MINUTE);
        String period = auxCalendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.getDefault());

        return day + " " + month + ", " + hour + "h" + minute + "m " + period;
    }

    public static String formatDate(Calendar date) {
        //date
        Calendar auxCalendar = date;
        String month = auxCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        int day = auxCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = auxCalendar.get(Calendar.HOUR);
        int minute = auxCalendar.get(Calendar.MINUTE);
        String period = auxCalendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.getDefault());

        return day + " " + month + ", " + hour + "h" + minute + "m " + period;
    }

    public static Date fromDateToApiFormat(Date date) {
        String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date dateReturn = null;
        try {
            dateReturn = formato.parse(dateString);

        } catch (ParseException pex) {
            pex.printStackTrace();
        }

        return dateReturn;
    }
}
