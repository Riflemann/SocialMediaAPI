package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.service.UserService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("{id}/posts")
public class PostsController {

    private final UserService userService;

    private final PostsService postsService;

    public PostsController(UserService userService, PostsService postsService) {
        this.userService = userService;
        this.postsService = postsService;
    }

    @GetMapping(value = "{friend_id}")
    public ResponseEntity<List<Posts>> getUserPosts(@PathVariable String friend_id){
        try {
            return ResponseEntity.ok(postsService.getAllByUserId(friend_id));
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "add", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Posts> addPost(@RequestParam String header,
                                         @RequestParam String text,
                                         @RequestParam("imageFile") MultipartFile pic,
                                         @PathVariable String id) throws IOException {
        String path = postsService.saveImage(pic);
        Posts post;
        try {
            post = Posts.builder()
                    .header(header)
                    .text(text)
                    .pic(path)
                    .userOwner(userService.getUserById(StringUtil.ValidationId(id)))
                    .creatingTime(LocalDateTime.now())
                    .build();
        } catch (UserNotFoundException | IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        postsService.savePost(post);
        return ResponseEntity.ok(post);
    }

    @PutMapping(value = "edit{post_id}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> editPost(@RequestParam(required = false) String header,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(name = "imageFile", required = false) MultipartFile pic,
                                           @PathVariable String post_id) throws AttributeNotFoundException, IOException {
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

    @DeleteMapping(value = "{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable String post_id) {
        try {
            postsService.deletePost(StringUtil.ValidationId(post_id));
            return ResponseEntity.ok("Пост удален");
        } catch (IncorrectIdException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
