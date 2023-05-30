package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.dto.UserPage;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    User getUserById(int id) throws UserNotFoundException;

    UserPage getUserPage(int id) throws UserNotFoundException;

    boolean sendRequest(int userIdFrom, int userIdTo) throws UserNotFoundException;

    List<Friends> getFriendsListById(int userId);

    List<Friends> getSubscribesListById(int userId);

    boolean acceptFriendRequest(int userIdFrom, int userIdTo);
}