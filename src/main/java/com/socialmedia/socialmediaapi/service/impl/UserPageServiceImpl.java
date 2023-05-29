package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.models.UserPage;
import com.socialmedia.socialmediaapi.service.UserPageService;
import com.socialmedia.socialmediaapi.service.PostsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPageServiceImpl implements UserPageService {

    private final PostsService postsService;

    public UserPageServiceImpl(PostsService postsService) {
        this.postsService = postsService;
    }

    @Override
    public UserPage getPage(Optional<User> user) {
        if (user.isPresent()) {
            return new UserPage(user.get(), postsService.getAllByUser(user));
        } else {
            return null;
        }

    }

}
