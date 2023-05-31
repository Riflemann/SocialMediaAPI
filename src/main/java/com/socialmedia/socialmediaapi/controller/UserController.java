package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.dto.UserPage;
import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.service.UserService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{id}")
@Tag(name="Контроллер для пользователей", description="Управляет действиями пользователей: добавление в друзья, вывод спиков друзей и подписчиков, вывод страницы пользователя")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Вывод страницы пользователя ",
            description = "Вывод страницы пользователя по средствам полученного ID пользователя",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден",
                            responseCode = "500"
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ResponseEntity<UserPage> getUserPage(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userService.getUserPage(id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Вывод списка друзей",
            description = "Вывод списка друзей по средствам полученного ID пользователя",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, переданный ID некорректный",
                            responseCode = "500"
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "friends")
    public ResponseEntity<List<Friends>> getFriendsList(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getFriendsListById(StringUtil.ValidationId(id)));
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Вывод списка подписчиков",
            description = "Вывод списка подписчиков по средствам полученного ID пользователя",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, переданный ID некорректный",
                            responseCode = "500"
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "subscribes")
    public ResponseEntity<List<Friends>> getSubscribesList(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getSubscribesListById(StringUtil.ValidationId(id)));
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Отправка запроса на добавления в друзья",
            description = "Отправка запроса на добавления в друзья по средствам полученного ID пользователя от кого завяка направляетсяи ID пользователя которому направляется заявка",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден, переданный ID некорректный",
                            responseCode = "500"
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "Принять запроса на добавления в друзья",
            description = "Отправка запроса на добавления в друзья по средствам полученного ID пользователя от кого завяка направляетсяи ID пользователя которому направляется заявка",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден, переданный ID некорректный",
                            responseCode = "500"
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "Удаление пользователя из списка друзей",
            description = "Удаление пользователя из списка друзей по средствам полученного ID пользователя от кого завяка на удаление ID пользователя которого нужно удалить",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, переданный ID некорректный",
                            responseCode = "500"
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
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



