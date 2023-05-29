package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.models.UserPage;

import java.util.Optional;

public interface UserPageService {

    UserPage getPage(Optional<User> user);
}
