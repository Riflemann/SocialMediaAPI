package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.models.UserPage;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import com.socialmedia.socialmediaapi.service.UserPageService;
import com.socialmedia.socialmediaapi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepo;

    private final UserPageService userPageService;

    public UserServiceImp(UserRepository userRepo, UserPageService userPageService) {
        this.userRepo = userRepo;
        this.userPageService = userPageService;
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> getUserById(int iD) {
        return userRepo.findById(iD);
    }

    @Override
    public UserPage getUserPage(int iD) {
        Optional<User> user = userRepo.findById(iD);

        return userPageService.getPage(user);
    }

}
