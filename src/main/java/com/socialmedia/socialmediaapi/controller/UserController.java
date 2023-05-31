package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.dto.UserPage;
import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.service.UserService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{id}")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, PostsService postsService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserPage> getUserPage(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userService.getUserPage(id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping(value = "friends")
    public ResponseEntity<List<Friends>> getFriendsList(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getFriendsListById(StringUtil.ValidationId(id)));
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "subscribes")
    public ResponseEntity<List<Friends>> getSubscribesList(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getSubscribesListById(StringUtil.ValidationId(id)));
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "friends{user_id_to}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable String id,
                                                    @PathVariable String user_id_to) {
        try {
            userService.sendRequest(StringUtil.ValidationId(id), StringUtil.ValidationId(user_id_to));
            return ResponseEntity.ok("Запрос отправлен");
        } catch (UserNotFoundException | IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "friends{user_id_to}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable String id,
                                                      @PathVariable String user_id_to) {
        try {
            userService.acceptFriendRequest(StringUtil.ValidationId(id), StringUtil.ValidationId(user_id_to));
            return ResponseEntity.ok("Запрос принят");
        } catch (IncorrectIdException | UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "friends{user_id_to}")
    public ResponseEntity<String> deleteFriend(@PathVariable String id,
                                               @PathVariable String user_id_to) {
        try {
            userService.deleteFromFriends(StringUtil.ValidationId(id), StringUtil.ValidationId(user_id_to));
            return ResponseEntity.ok("Друг удален");
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}



