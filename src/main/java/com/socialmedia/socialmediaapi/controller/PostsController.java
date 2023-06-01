package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.service.UserService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
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
@Tag(name="Контроллер для работы с постами пользователей", description="Управляет действиями пользователей по добавлению, удалению, просмотров постов")
public class PostsController {

    private final UserService userService;

    private final PostsService postsService;

    public PostsController(UserService userService, PostsService postsService) {
        this.userService = userService;
        this.postsService = postsService;
    }


    @Operation(
            summary = "Получить страницу с постами друзей и подписчиков, поддерживает пагинацию и сортировку по времени создания постов",
            description = "Получить страницу с постами по средствам полученного ID пользователя создавший пост, номер страницы, лимит постов на странице",
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
    @GetMapping()
    public ResponseEntity<List<Posts>> getFriendsPosts(@RequestParam(required = false, value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                                       @RequestParam(required = false, value = "limit", defaultValue = "20") Integer limit,
                                                       @PathVariable String id) throws UserNotFoundException, IncorrectIdException {
        return ResponseEntity.ok(postsService.getAllPostsFromFriends(id, offset, limit));

    }

    @Operation(
            summary = "Получить страницу с постами, поддерживает пагинацию и сортировку по времени создания постов",
            description = "Получить страницу с постами по средствам полученного ID пользователя создавший пост, номер страницы, лимит постов на странице",
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
    @GetMapping(value = "my_posts")
    public ResponseEntity<List<Posts>> getUserPosts(@PathVariable String id) throws IncorrectIdException {
        return ResponseEntity.ok(postsService.getAllByUserId(id));
    }

    @Operation(
            summary = "Получить страницу с постами, поддерживает пагинацию и сортировку по времени создания постов",
            description = "Получить страницу с постами по средствам полученного ID пользователя создавший пост, номер страницы, лимит постов на странице",
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
    @PostMapping(value = "add", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Posts> addPost(@RequestParam String header,
                                         @RequestParam String text,
                                         @RequestParam("imageFile") MultipartFile pic,
                                         @PathVariable String id) throws IOException, IncorrectIdException, UserNotFoundException {
        String path = postsService.saveImage(pic);
        Posts post;
        post = Posts.builder()
                .header(header)
                .text(text)
                .pic(path)
                .userOwner(userService.getUserById(StringUtil.ValidationId(id)))
                .creatingTime(LocalDateTime.now())
                .build();
        postsService.savePost(post);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Изменить созданный пост",
            description = "Изменить созданный пост по средствам полученного ID поста",
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
    @PutMapping(value = "edit{post_id}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> editPost(@RequestParam(required = false) String header,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(name = "imageFile", required = false) MultipartFile pic,
                                           @PathVariable String post_id) throws AttributeNotFoundException, IOException, IncorrectIdException {
        String pathToImage = "";
        if (pic.isEmpty()) {
            pathToImage = postsService.getPic(post_id);
        } else {
            pathToImage = postsService.saveImage(pic);
        }
        postsService.editPost(header, text, pathToImage, post_id);
        return ResponseEntity.ok("Пост изменен");
    }

    @Operation(
            summary = "Удалить пост",
            description = "Удалить пост по средствам полученного ID поста",
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
    @DeleteMapping(value = "{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable String post_id) throws IncorrectIdException {
            postsService.deletePost(StringUtil.ValidationId(post_id));
            return ResponseEntity.ok("Пост удален");
    }
}
