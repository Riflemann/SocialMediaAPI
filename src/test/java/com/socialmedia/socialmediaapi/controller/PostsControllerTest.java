package com.socialmedia.socialmediaapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.Role;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostsService postsService;

    @Autowired
    private UserService userService;

    static User userOne;
    static User userTwo;
    static User userThree;
    static User userFour;

    @BeforeAll
    static void setUp() {
        userOne = User.builder().fullName("John").city("City").login("John").password("John").role(Role.USER).build();
        userTwo = User.builder().fullName("Mat").city("City").login("Mat").password("Mat").role(Role.USER).build();
        userThree = User.builder().fullName("Pit").city("City").login("Pit").password("Pit").role(Role.USER).build();
        userFour = User.builder().fullName("Dan").city("City").login("Dan").password("Dan").role(Role.USER).build();

    }

    @Test
    public void addPost() {
        User savedUserOne = userService.save(userOne);
        User savedUserTwo = userService.save(userTwo);
        User savedUserThree = userService.save(userThree);
        User savedUserFour = userService.save(userFour);

        postsService.savePost(Posts.builder().userOwner(savedUserOne).header("a").text("a").creatingTime(LocalDateTime.now()).build());
        postsService.savePost(Posts.builder().userOwner(savedUserTwo).header("aa").text("aa").creatingTime(LocalDateTime.now()).build());
        postsService.savePost(Posts.builder().userOwner(savedUserThree).header("aaa").text("aaa").creatingTime(LocalDateTime.now()).build());
        postsService.savePost(Posts.builder().userOwner(savedUserFour).header("aaaa").text("aaaa").creatingTime(LocalDateTime.now()).build());
    }
    @Test
    void givenValidUserId_whenGetFriendsPosts_thenReturnsPosts() throws Exception {

        // Given
        String userId = "valid_user_id";
        int offset = 0;
        int limit = 10;

        // When
        MvcResult mvcResult = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/{id}/posts?offset={offset}&limit={limit}", userId, offset, limit))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // Then
        List<Posts> posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(posts);
        assertFalse(posts.isEmpty());

    }


    @Test
    void getUserPosts() {
    }

    @Test
    void editPost() {
    }

    @Test
    void deletePost() {
    }
}