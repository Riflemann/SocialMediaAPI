package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.dto.UserPage;
import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController должен")
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    @DisplayName("выводить страницу пользователя")
    public void shouldGetUserPage() throws UserNotFoundException {
        //Arrange
        int id = 1;
        UserPage userPage = new UserPage();
        when(userService.getUserPage(id)).thenReturn(userPage);

        //Act
        ResponseEntity<UserPage> response = userController.getUserPage(id);

        //Assert
        assertEquals(userPage, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("выводить список друзей")
    public void shouldGetFriendsList() throws IncorrectIdException, UserNotFoundException {
        //Arrange
        int id = 1;
        String idStr="1";
        List<Friends> friendsList = new ArrayList<>();
        when(userService.getFriendsListById(id)).thenReturn(friendsList);

        //Act
        ResponseEntity<List<Friends>> response = userController.getFriendsList(idStr);

        //Assert
        assertEquals(friendsList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("выводить список подписчиков")
    public void shouldGetSubscribesList() throws IncorrectIdException, UserNotFoundException {
        //Arrange
        int id = 1;
        String idStr = "1";
        List<Friends> subscribesList = new ArrayList<>();
        when(userService.getSubscribesListById(id)).thenReturn(subscribesList);

        //Act
        ResponseEntity<List<Friends>> response = userController.getSubscribesList(idStr);

        //Assert
        assertEquals(subscribesList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Тест метода sendFriendRequest")
    public void testSendFriendRequest() throws UserNotFoundException, IncorrectIdException {
        //Arrange
        int id = 1;
        int user_id_to = 2;
        String idStr = "1";
        String user_id_toStr = "2";
        when(userService.sendRequest(id, user_id_to)).thenReturn(true);

        //Act
        ResponseEntity<String> response = userController.sendFriendRequest(idStr, user_id_toStr);

        //Assert
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Запрос отправлен");
    }

    // Аннотация для обозначения тестового метода
    @Test
    @DisplayName("Тест метода acceptFriendRequest")
    public void testAcceptFriendRequest() throws UserNotFoundException, IncorrectIdException {
        //Arrange
        int id = 1;
        int user_id_to = 2;
        String idStr = "1";
        String user_id_toStr = "2";
        when(userService.acceptFriendRequest(id, user_id_to)).thenReturn(true);

        //Act
        ResponseEntity<String> response = userController.acceptFriendRequest(idStr, user_id_toStr);

        //Assert
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Запрос принят");
    }

    @Test
    @DisplayName("Тест метода deleteFriend")
    public void testDeleteFriend() throws UserNotFoundException, IncorrectIdException {
        //Arrange
        int id = 1;
        int user_id_to = 2;
        String idStr = "1";
        String user_id_toStr = "2";
        when(userService.deleteFromFriends(id, user_id_to)).thenReturn(true);

        //Act
        ResponseEntity<String> response = userController.deleteFriend(idStr, user_id_toStr);

        //Assert
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Друг удален");
    }
}
