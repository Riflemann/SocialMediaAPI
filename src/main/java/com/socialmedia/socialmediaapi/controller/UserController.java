package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.dto.UserPage;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.service.UserService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final PostsService postsService;

    public UserController(UserService userService, PostsService postsService) {
        this.userService = userService;
        this.postsService = postsService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserPage> getPage(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userService.getUserPage(id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "{id}/post/add", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Posts> addPost(@RequestParam String header,
                                         @RequestParam String text,
                                         @RequestParam("imageFile") MultipartFile pic,
                                         @PathVariable String id) throws IOException {
        String path = postsService.saveImage(pic);

        Posts post = null;
        try {
            StringUtil.ValidationId(id);

            post = Posts.builder()
                    .header(header)
                    .text(text)
                    .pic(path)
                    .userOwner(userService.getUserById(Integer.parseInt(id)))
                    .creatingTime(LocalDateTime.now())
                    .build();
        } catch (UserNotFoundException | IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        postsService.savePost(post);
        return ResponseEntity.ok(post);
    }

    @PutMapping(value = "{id}/post/edit{post_id}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> editPost(@RequestParam(required = false) String header,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(name = "imageFile", required = false) MultipartFile pic,
                                           @PathVariable String post_id, @PathVariable String id) throws AttributeNotFoundException, IOException {
        String pathToImage = "";

        if (pic.isEmpty()) {
            try {
                pathToImage = postsService.getPic(post_id);
            } catch (IncorrectIdException e) {
                e.printStackTrace();
                ResponseEntity.badRequest().body(e);
            }
        } else {
            pathToImage = postsService.saveImage(pic);
        }

        postsService.editPost(header, text, pathToImage, post_id);
        return ResponseEntity.ok("Пост изменен");
    }
}
