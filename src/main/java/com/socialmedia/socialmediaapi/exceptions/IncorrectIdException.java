package com.socialmedia.socialmediaapi.exceptions;

public class IncorrectIdException extends Exception {
    String message;

    public IncorrectIdException(String str) {
        message = str;
    }

    public String toString() {
        return (message);
    }
}

