package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Messages;
import com.socialmedia.socialmediaapi.service.MessagesService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "{user_id}/messages")
public class MessagesController {

    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping(value = "outbox")
    public ResponseEntity<List<Messages>> getMessagesFromUser(@PathVariable int user_id) {
        try {
            return ResponseEntity.ok(messagesService.getAllMessagesFromUser(user_id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "inbox")
    public ResponseEntity<List<Messages>> getMessagesForUser(@PathVariable int user_id) {
        try {
            return ResponseEntity.ok(messagesService.getAllMessagesForUser(user_id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "send{id_user_to}")
    public ResponseEntity<String> sendMessage(@PathVariable String user_id,
                                              @PathVariable String id_user_to,
                                              @RequestParam String textMessage) {
        try {
            messagesService.sendMessage(StringUtil.ValidationId(user_id),
                                        StringUtil.ValidationId(id_user_to),
                                        textMessage);
            return ResponseEntity.ok("Сообщение отправлено");
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
