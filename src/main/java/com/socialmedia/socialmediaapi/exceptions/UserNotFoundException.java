package com.socialmedia.socialmediaapi.exceptions;

public class UserNotFoundException extends Exception {
    String message;

    public UserNotFoundException(String str) {
        message = str;
    }

    public String toString() {
        return (message);
    }
}
