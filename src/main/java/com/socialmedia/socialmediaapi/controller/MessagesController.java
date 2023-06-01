package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Messages;
import com.socialmedia.socialmediaapi.service.MessagesService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "{user_id}/messages")
@Tag(name="Контроллер для сообщений", description="Управляет действиями пользователей по отправке, получению сообщений")
public class MessagesController {

    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Operation(
            summary = "Получить исходящие сообщения",
            description = "Получить исходящие сообщения по средствам полученного ID пользователя",
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
    @GetMapping(value = "outbox")
    public ResponseEntity<List<Messages>> getMessagesFromUser(@PathVariable int user_id) throws UserNotFoundException {
        return ResponseEntity.ok(messagesService.getAllMessagesFromUser(user_id));
    }

    @Operation(
            summary = "Получить входящие сообщения",
            description = "Получить входящие сообщения по средствам полученного ID пользователя",
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
    @GetMapping(value = "inbox")
    public ResponseEntity<List<Messages>> getMessagesForUser(@PathVariable int user_id) throws UserNotFoundException {
        return ResponseEntity.ok(messagesService.getAllMessagesForUser(user_id));
    }

    @Operation(
            summary = "Отправить сообщение",
            description = "Отправить сообщение, принимает ID пользователь от кого сообщение, ID пользователя кому сообщение текст сообщения",
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
                    )
            }

    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "send{id_user_to}")
    public ResponseEntity<String> sendMessage(@PathVariable String user_id,
                                              @PathVariable String id_user_to,
                                              @RequestParam String textMessage) throws IncorrectIdException {
        messagesService.sendMessage(StringUtil.ValidationId(user_id),
                StringUtil.ValidationId(id_user_to),
                textMessage);
        return ResponseEntity.ok("Сообщение отправлено");

    }


}
