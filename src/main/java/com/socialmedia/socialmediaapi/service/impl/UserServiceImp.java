package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.dto.UserPage;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.FriendsRepository;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import com.socialmedia.socialmediaapi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepo;

    private final FriendsRepository friendsRepo;


    public UserServiceImp(UserRepository userRepo, FriendsRepository friendsRepo) {
        this.userRepo = userRepo;
        this.friendsRepo = friendsRepo;
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepo.findById(id);
    }

    @Override
    public UserPage getUserPage(int id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return new UserPage(
                    user.get().getFullName(),
                    user.get().getCity(),
                    user.get().getPostsList(),
                    user.get().getFriendsList());
        } else {
//            todo реализовать свое исключение
            throw new RuntimeException();
        }
    }

    @Override
    public boolean sendRequest(int userIdFrom, int userIdTo) {
        Optional<User> userFrom = userRepo.findById(userIdFrom);
        Optional<User> userTo = userRepo.findById(userIdTo);
        if (userFrom.isPresent() && userTo.isPresent()) {
            Friends friends = Friends.builder()
                    .userTo(userFrom.get())
                    .userFrom(userTo.get())
                    .status(0)
                    .build();
            friendsRepo.save(friends);
        } else {
//            todo реализовать свое исключение
            throw new RuntimeException();
        }
        return true;
    }

    @Override
    public List<Friends> getFriendsListById(int userId) {
        return friendsRepo.getAllFriends(userId);
    }

    @Override
    public List<Friends> getSubscribesListById(int userId) {
        return friendsRepo.getAllSubscribes(userId);
    }

    @Override
    public boolean acceptFriendRequest(int userIdFrom, int userIdTo) {
        friendsRepo.acceptFriendRequest(userIdFrom, userIdTo);
        return true;
    }

}
