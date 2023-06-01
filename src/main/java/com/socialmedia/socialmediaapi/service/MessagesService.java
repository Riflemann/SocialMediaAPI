package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Messages;

import java.util.List;

public interface MessagesService {

    List<Messages> getAllMessagesForUser(int userId) throws UserNotFoundException;

    List<Messages> getAllMessagesFromUser(int userId) throws UserNotFoundException;

    void sendMessage(int userIdFrom, int userTo, String msg);
}
