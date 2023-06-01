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
                            responseCode = "404"
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ResponseEntity<UserPage> getUserPage(@PathVariable int id) throws UserNotFoundException {
            return ResponseEntity.ok(userService.getUserPage(id));
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
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден, переданный ID некорректный",
                            responseCode = "404"
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "friends")
    public ResponseEntity<List<Friends>> getFriendsList(@PathVariable String id) throws IncorrectIdException, UserNotFoundException {
            return ResponseEntity.ok(userService.getFriendsListById(StringUtil.ValidationId(id)));
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
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден, переданный ID некорректный",
                            responseCode = "404"
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "subscribes")
    public ResponseEntity<List<Friends>> getSubscribesList(@PathVariable String id) throws IncorrectIdException, UserNotFoundException {
            return ResponseEntity.ok(userService.getSubscribesListById(StringUtil.ValidationId(id)));
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
                            description = "Internal Server Error, переданный ID некорректный",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден, переданный ID некорректный",
                            responseCode = "404"
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "friends{user_id_to}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable String id,
                                                    @PathVariable String user_id_to) throws UserNotFoundException, IncorrectIdException {
            userService.sendRequest(StringUtil.ValidationId(id), StringUtil.ValidationId(user_id_to));
            return ResponseEntity.ok("Запрос отправлен");
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
                            description = "Internal Server Error, переданный ID некорректный",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден, переданный ID некорректный",
                            responseCode = "404"
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "friends{user_id_to}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable String id,
                                                      @PathVariable String user_id_to) throws UserNotFoundException, IncorrectIdException {
            userService.acceptFriendRequest(StringUtil.ValidationId(id), StringUtil.ValidationId(user_id_to));
            return ResponseEntity.ok("Запрос принят");
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
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error, Пользователь под таким ID не найден, переданный ID некорректный",
                            responseCode = "404"
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = "friends{user_id_to}")
    public ResponseEntity<String> deleteFriend(@PathVariable String id,
                                               @PathVariable String user_id_to) throws IncorrectIdException, UserNotFoundException {
            userService.deleteFromFriends(StringUtil.ValidationId(id), StringUtil.ValidationId(user_id_to));
            return ResponseEntity.ok("Друг удален");
    }

}



