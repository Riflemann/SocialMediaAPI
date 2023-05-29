package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepo;

    public UserServiceImp(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

}
