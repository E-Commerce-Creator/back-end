package com.e_commerce_creator.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {


    public static Date stringToDate(String dateString) {
        return stringToDate(dateString, "yyyy-MM-dd");
    }


    public static Date stringToDateTime(String dateString) {
        return stringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
    }


    public static Date stringToDateWithAMPM(String dateString) {
        return stringToDate(dateString, "yyyy-MM-dd hh:mm a");
    }


    public static Date stringToDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean isBeforeToday(Date date) {
        Date today = new Date();
        return date.before(today);
    }


    public static boolean isBeforeToday(String dateString) {
        Date inputDate = stringToDate(dateString);
        Date today = new Date();
        return inputDate != null && inputDate.before(today);
    }

    public static boolean isAfterToday(String dateString) {
        Date inputDate = stringToDate(dateString);
        Date today = new Date();
        return inputDate != null && inputDate.after(today);
    }


    public static int compareDates(Date date1, Date date2) {
        return date1.compareTo(date2);
    }


    public static int compareDates(String dateString1, String dateString2, String format) {
        Date date1 = stringToDate(dateString1, format);
        Date date2 = stringToDate(dateString2, format);
        return compareDates(date1, date2);
    }


    public static int compare(String str1, String str2, String format) {
        Date date1 = stringToDate(str1, format);
        Date date2 = stringToDate(str2, format);
        return compareDates(date1, date2);
    }


    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }


    public static int calculateAge(String birthDateString) {
        LocalDate birthDate = LocalDate.parse(birthDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return calculateAge(birthDate);
    }


    public static int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears(); // Returns full period between birthDate and currentDate
    }
}
