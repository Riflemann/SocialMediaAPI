package com.socialmedia.socialmediaapi.utils;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;

import java.text.NumberFormat;
import java.text.ParseException;

public class StringUtil {

    public static int ValidationId(String str) throws IncorrectIdException {
        try {
            NumberFormat.getInstance().parse(str);
            System.out.println(str + " is correct ID");
            return Integer.parseInt(str);
        } catch (ParseException e) {
            throw new IncorrectIdException("Incorrect ID. " + str + " is not a number");

        }
    }
}
