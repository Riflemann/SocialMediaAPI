package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.dto.UserPage;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    Optional<User> getUserById(int iD);

    UserPage getUserPage(int iD);

    boolean sendRequest(int userIdFrom, int userIdTo);

    List<Friends> getFriendsListById(int userId);

    List<Friends> getSubscribesListById(int userId);

    boolean acceptFriendRequest(int userIdFrom, int userIdTo);
}
