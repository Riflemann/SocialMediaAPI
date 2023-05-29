package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.UserPage;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.AttributeNotFoundException;
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
    public ResponseEntity<UserPage> getPage(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserPage(Integer.parseInt(id)));
    }

    @PostMapping(value = "{id}/post/add", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Posts> addPost(@RequestParam String header,
                                         @RequestParam String text,
                                         @RequestParam("imageFile") MultipartFile pic,
                                         @PathVariable String id) {
        String path = postsService.saveImage(pic);

        if (path == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Posts post = Posts.builder()
                .header(header)
                .text(text)
                .pic(path)
                .userOwner(userService.getUserById(Integer.parseInt(id)).get())
                .creatingTime(LocalDateTime.now())
                .build();

        postsService.savePost(post);
        return ResponseEntity.ok(post);
    }

    @PutMapping(value = "{id}/post/edit{post_id}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> editPost(@RequestParam(required = false) String header,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(name = "imageFile", required = false) MultipartFile pic,
                                           @PathVariable String post_id, @PathVariable String id) throws AttributeNotFoundException {
        String pathToImage;

        if (pic.isEmpty()) {
            pathToImage = postsService.getPic(post_id);
        } else {
            String path = postsService.saveImage(pic);
            if (path == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            pathToImage = path;
        }

        postsService.editPost(header, text, pathToImage, post_id);
        return ResponseEntity.ok("Пост изменен");
    }
}
