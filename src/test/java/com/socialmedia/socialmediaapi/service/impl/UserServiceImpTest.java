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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("User Service Test")
class UserServiceImpTest {

    @Mock
    UserRepository userRepo;

    @Mock
    FriendsRepository friendsRepo;

    @InjectMocks
    UserServiceImp userServiceImp;

    List<Integer> userIdList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userIdList.add(1);
        userIdList.add(2);
    }

    @Test
    @DisplayName("Test save user")
    void testSaveUser() {
        User user = User.builder().id(1).fullName("John Doe").city("New York").build();
        when(userRepo.save(user)).thenReturn(user);
        User savedUser = userServiceImp.save(user);
        Assertions.assertEquals(user, savedUser);
        verify(userRepo, times(1)).save(user);
    }

    @Test
    @DisplayName("Test get all users")
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(User.builder().id(1).fullName("John Doe").city("New York").build());
        userList.add(User.builder().id(2).fullName("Jane Doe").city("Los Angeles").build());
        when(userRepo.findAll()).thenReturn(userList);
        List<User> users = userServiceImp.getAll();
        Assertions.assertEquals(userList, users);
        verify(userRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("Test get user by id")
    void testGetUserById() throws UserNotFoundException {
        User user = User.builder().id(1).fullName("John Doe").city("New York").build();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        User foundUser = userServiceImp.getUserById(1);
        Assertions.assertEquals(user, foundUser);
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test get user page")
    void testGetUserPage() throws UserNotFoundException {
        User user = User.builder().id(1).fullName("John Doe").city("New York").build();
        List<Posts> postList = new ArrayList<>();
        List<Friends> friendsList = new ArrayList<>();
        UserPage userPage = new UserPage("John Doe", "New York", postList, friendsList);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserPage foundUserPage = userServiceImp.getUserPage(1);
        Assertions.assertEquals(userPage, foundUserPage);
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test send friend request")
    void testSendFriendRequest() throws UserNotFoundException {
        when(userRepo.getAllUsersId()).thenReturn(userIdList);
        userServiceImp.sendRequest(1, 2);
        verify(friendsRepo, times(1)).save(any(Friends.class));
    }

    @Test
    @DisplayName("Test get friends list by id")
    void testGetFriendsListById() throws UserNotFoundException {
        List<Friends> friendsList = new ArrayList<>();
        when(userRepo.getAllUsersId()).thenReturn(userIdList);
        when(friendsRepo.getAllFriends(1)).thenReturn(friendsList);
        List<Friends> foundFriendsList = userServiceImp.getFriendsListById(1);
        Assertions.assertEquals(friendsList, foundFriendsList);
        verify(friendsRepo, times(1)).getAllFriends(1);
    }

    @Test
    @DisplayName("Test get subscribes list by id")
    void testGetSubscribesListById() throws UserNotFoundException {
        List<Friends> subscribesList = new ArrayList<>();
        when(userRepo.getAllUsersId()).thenReturn(userIdList);
        when(friendsRepo.getAllSubscribes(1)).thenReturn(subscribesList);
        List<Friends> foundSubscribesList = userServiceImp.getSubscribesListById(1);
        Assertions.assertEquals(subscribesList, foundSubscribesList);
        verify(friendsRepo, times(1)).getAllSubscribes(1);
    }

    @Test
    @DisplayName("Test accept friend request")
    void testAcceptFriendRequest() throws UserNotFoundException {
        when(userRepo.getAllUsersId()).thenReturn(userIdList);
        userServiceImp.acceptFriendRequest(1, 2);
        verify(friendsRepo, times(1)).acceptFriendRequest(1, 2);
        verify(friendsRepo, times(1)).save(any(Friends.class));
    }

    @Test
    @DisplayName("Test delete from friends")
    void testDeleteFromFriends() throws UserNotFoundException {
        when(userRepo.getAllUsersId()).thenReturn(userIdList);
        userServiceImp.deleteFromFriends(1, 2);
        verify(friendsRepo, times(1)).deleteFromFriends(1, 2);
        verify(friendsRepo, times(1)).changeStatusFromFriendToSubscribe(2, 1);
    }

    @Test
    @DisplayName("Test validation id user")
    void testValidationIdUser() throws UserNotFoundException {
        when(userRepo.getAllUsersId()).thenReturn(userIdList);
        userServiceImp.validationIdUser(1, 2);
        verify(userRepo, times(1)).getAllUsersId();
    }

    @Test
    @DisplayName("Test user not found exception")
    void testUserNotFoundException() {
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> userServiceImp.getUserById(3));
        Assertions.assertEquals("Пользователь с ID 3 не найден", exception.getMessage());
    }
}