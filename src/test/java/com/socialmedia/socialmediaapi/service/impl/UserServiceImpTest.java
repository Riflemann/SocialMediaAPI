package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.dto.UserPage;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.FriendsRepository;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceImpTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private FriendsRepository friendsRepo;

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<User> userList = new ArrayList<>();
        userList.add(User.builder()
                                .fullName("John Doe")
                                .city("New York")
                                .build());
        userList.add(User.builder()
                                .fullName("Jane Doe")
                                .city("Los Angeles")
                                .build());
        when(userRepo.findAll()).thenReturn(userList);
        List<User> result = userService.getAll();
        Assertions.assertEquals(2, result.size());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    public void testGetUserById() throws UserNotFoundException {
        User user = User.builder()
                                .id(1)
                                .fullName("John Doe")
                                .city("New York")
                                .build();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        User result = userService.getUserById(1);
        Assertions.assertEquals(user, result);
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    public void testGetUserByIdThrowsException() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));
        verify(userRepo, times(1)).findById(1);
    }

//    @Test
//    public void testGetUserPage() throws UserNotFoundException {
//        User userOne = User.builder()
//                                    .fullName("John Doe")
//                                    .city("New York")
//                                    .build();
//        User userTwo = User.builder()
//                                    .fullName("Jane Doe")
//                                    .city("Los Angeles")
//                                    .build();
//        List<Posts> postList = new ArrayList<>();
//        postList.add(Posts.builder()
//                                    .creatingTime(LocalDateTime.now())
//                                    .header("First post")
//                                    .text("First post")
//                                    .build());
//        List<Friends> friendsList = new ArrayList<>();
//        friendsList.add(new Friends(1, userOne, userTwo, 0));
//        when(userRepo.findById(1)).thenReturn(Optional.of(userOne));
//        when(friendsRepo.getAllFriends(1)).thenReturn(friendsList);
//        when(userOne.getPostsList()).thenReturn(postList);
//        UserPage result = userService.getUserPage(1);
//        Assertions.assertEquals(userOne.getFullName(), result.getFullName());
//        Assertions.assertEquals(userOne.getCity(), result.getCity());
//        Assertions.assertEquals(postList, result.getPosts());
//        Assertions.assertEquals(friendsList, result.getFriends());
//        verify(userRepo, times(1)).findById(1);
//        verify(friendsRepo, times(1)).getAllFriends(1);
//        verify(userOne, times(1)).getPostsList();
//    }

    @Test
    public void testGetUserPageThrowsException() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUserPage(1);
        });
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    public void testSendRequest() throws UserNotFoundException {
        User userFrom = User.builder()
                                    .fullName("John Doe")
                                    .city("New York")
                                    .build();
        User userTo = User.builder()
                                    .fullName("Jane Doe")
                                    .city("Los Angeles")
                                    .build();
        when(userRepo.findById(1)).thenReturn(Optional.of(userFrom));
        when(userRepo.findById(2)).thenReturn(Optional.of(userTo));
        boolean result = userService.sendRequest(1, 2);
        Assertions.assertTrue(result);
        verify(userRepo, times(1)).findById(1);
        verify(userRepo, times(1)).findById(2);
        verify(friendsRepo, times(1)).save(any(Friends.class));
    }

    @Test
    public void testSendRequestThrowsException() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.sendRequest(1, 2);
        });
        verify(userRepo, times(1)).findById(1);
        verify(userRepo, times(1)).findById(2);
        verify(friendsRepo, times(0)).save(any(Friends.class));
    }

    @Test
    public void testGetFriendsListById() {
        List<Friends> friendsList = new ArrayList<>();
        friendsList.add(
                new Friends
        (
                        1,
                        User.builder()
                                .fullName("John Doe")
                                .city("New York")
                                .build(),
                        User.builder()
                                .fullName("Jane Doe")
                                .city("Los Angeles")
                                .build(),
                        1)
        );
        when(friendsRepo.getAllFriends(1)).thenReturn(friendsList);
        List<Friends> result = userService.getFriendsListById(1);
        Assertions.assertEquals(friendsList, result);
        verify(friendsRepo, times(1)).getAllFriends(1);
    }

    @Test
    public void testGetSubscribesListById() {
        List<Friends> subscribesList = new ArrayList<>();
        subscribesList.add
        (
                new Friends(
                        1,
                        User.builder()
                                .fullName("John Doe")
                                .city("New York")
                                .build(),
                        User.builder()
                                .fullName("Jane Doe")
                                .city("Los Angeles")
                                .build(),
                        0)
        );
        when(friendsRepo.getAllSubscribes(1)).thenReturn(subscribesList);
        List<Friends> result = userService.getSubscribesListById(1);
        Assertions.assertEquals(subscribesList, result);
        verify(friendsRepo, times(1)).getAllSubscribes(1);
    }

    @Test
    public void testAcceptFriendRequest() throws UserNotFoundException {
        userService.acceptFriendRequest(1, 2);
        verify(friendsRepo, times(1)).acceptFriendRequest(1, 2);
    }
}