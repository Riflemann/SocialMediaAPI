package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.models.UserPage;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    Optional<User> getUserById(int iD);

    UserPage getUserPage(int iD);
}
