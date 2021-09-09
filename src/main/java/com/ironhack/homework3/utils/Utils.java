package com.ironhack.homework3.utils;

import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isValidPositiveNumber(String num) {
        // Method to validate a number inputted by the user
        try {
            int convertedNum = Integer.parseInt(num);
            return convertedNum >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to validate a Product inputted by the user
    public static boolean validProduct(String product) {
        try {
            Product.valueOf(product);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to validate a Industry inputted by the user
    public static boolean validIndustry(String industry) {
        try {
            Industry.valueOf(industry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to validate a name inputted by the user
    public static boolean validName(String name) {
        return Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$").matcher(name).find();
    }

    // Method to validate an email inputted by the user
    public static boolean validEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return emailPattern.matcher(email).find();
    }

    // Method to validate a phone number inputted by the user
    public static boolean validPhone(String phone) {
        String patterns = "^\\s?((\\+[1-9]{1,4}[ \\-]*)" +
                "|(\\([0-9]{2,3}\\)[ \\-]*)" +
                "|([0-9]{2,4})[ \\-]*)*?[0-9]{3,4}?[ \\-]*[0-9]{3,4}?\\s?";
        Pattern phoneNumRegexes = Pattern.compile(patterns);
        return phoneNumRegexes.matcher(phone).find();
    }

    // Method to validate a generic string inputted by the user
    public static boolean validLocation(String str) {
        if (str.length() < 1) return false;
        return Pattern.compile("^[A-ZÀ-Ü]+([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]+$").matcher(str).find();
    }


    // Check for the command syntax in terms of number of parameters and validity of parameters
    public static boolean validCommand(String command) {

        String[] commandWords = command.trim().toLowerCase().split(" ");
        if (commandWords.length > 1) {
            switch (commandWords[0]) {

                case "new":
                    switch (commandWords[1]){
                        case "lead":
                        case "salesrep":
                            return commandWords.length == 2;
                    }
                    return false;
                case "show":
                    switch (commandWords[1]) {
                        case "leads":
                        case "opportunities":
                        case "accounts":
                        case "contacts":
                        case "salesrep":
                            return commandWords.length == 2;
                    }
                    return false;
                case "lookup":
                    if (commandWords.length == 3) {
                        switch (commandWords[1]) {
                            case "lead":
                            case "opportunity":
                            case "account":
                            case "contact":
                            case "salesrep":
                                return isValidPositiveNumber(commandWords[2]);
                        }
                    }
                    return false;
                case "convert":
                case "close-won":
                case "close-lost":
                    return isValidPositiveNumber(commandWords[1]);
                case "help":
                    if (commandWords[1].equals("-a") && commandWords.length == 2)
                        return true;
                default:
                    return false;
            }
        } else {
            switch (commandWords[0]) {
                case "help":
                case "exit":
//                case "save":
                    return true;
                default:
                    return false;
            }
        }
    }

    public static double getMedianValue(List<Integer> list) {
        ArrayList<Integer> arrayList = new ArrayList<>(list);
        System.out.println(arrayList);
        Collections.sort(arrayList);
        System.out.println(arrayList);
        var length = arrayList.size();
        if (length % 2 == 0) return (double) (arrayList.get(length / 2) + arrayList.get(length / 2 - 1)) / 2.0;
        return (double) arrayList.get(length / 2);
    }
}
