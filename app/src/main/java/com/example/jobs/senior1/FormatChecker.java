package com.example.jobs.senior1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jobs_dell on 11-Mar-17.
 */

public class FormatChecker {
    private FormatChecker(){

    }
    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    public static boolean isWebsite(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+.([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    public static boolean isName(String email) {
        boolean isValid = true;

        if (email.trim().compareTo("")==0) {
            isValid = false;
            return isValid;
        }
        if (email.matches(".*\\d+.*")) {
            return isValid;
        }
        return isValid;
    }
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
