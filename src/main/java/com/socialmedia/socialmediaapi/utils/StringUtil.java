package com.socialmedia.socialmediaapi.utils;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;

import java.text.NumberFormat;
import java.text.ParseException;

public class StringUtil {

    public static boolean ValidationId(String str) throws IncorrectIdException {
        try {
            NumberFormat.getInstance().parse(str);
            System.out.println(str + " is correct ID");
            return true;
        } catch (ParseException e) {
            throw new IncorrectIdException("Incorrect ID. " + str + " is not a number");

        }
    }
}
