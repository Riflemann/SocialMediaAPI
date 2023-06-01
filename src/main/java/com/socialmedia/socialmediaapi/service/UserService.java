package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.dto.UserPage;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    List<User> getAll();

    User getUserById(int id) throws UserNotFoundException;

    UserPage getUserPage(int id) throws UserNotFoundException;

    boolean sendRequest(int userIdFrom, int userIdTo) throws UserNotFoundException;

    List<Friends> getFriendsListById(int userId) throws UserNotFoundException;

    List<Friends> getSubscribesListById(int userId) throws UserNotFoundException;

    boolean acceptFriendRequest(int userIdFrom, int userIdTo) throws UserNotFoundException;

    boolean deleteFromFriends(int userIdFrom, int userIdTo) throws UserNotFoundException;
}
