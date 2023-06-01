package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.FriendsRepository;
import com.socialmedia.socialmediaapi.repository.PostsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PostsServiceImpTest {
    @Mock
    private PostsRepository postsRepo;

    @Mock
    private FriendsRepository friendsRepo;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    private PostsServiceImp postsServiceImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        postsServiceImp = new PostsServiceImp(postsRepo, friendsRepo, entityManagerFactory);
    }

    @Test
    @DisplayName("Test getAllByUser when user is present")
    void testGetAllByUserWhenUserIsPresent() {
        User user = new User();
        user.setId(1);
        List<Posts> postsList = new ArrayList<>();
        postsList.add(new Posts());
        when(postsRepo.getAllByUserOwner(user)).thenReturn(postsList);
        List<Posts> result = postsServiceImp.getAllByUser(Optional.of(user));
        Assertions.assertEquals(postsList, result);
    }

    @Test
    @DisplayName("Test getAllByUser when user is not present")
    void testGetAllByUserWhenUserIsNotPresent() {
        List<Posts> result = postsServiceImp.getAllByUser(Optional.empty());
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    @DisplayName("Test getAllByUserId when id is correct")
    void testGetAllByUserIdWhenIdIsCorrect() throws IncorrectIdException {
        List<Posts> postsList = new ArrayList<>();
        postsList.add(new Posts());
        when(postsRepo.getAllByUserOwnerId(anyInt())).thenReturn(postsList);
        List<Posts> result = postsServiceImp.getAllByUserId("1");
        Assertions.assertEquals(postsList, result);
    }

    @Test
    @DisplayName("Test getAllByUserId when id is incorrect")
    void testGetAllByUserIdWhenIdIsIncorrect() {
        Assertions.assertThrows(IncorrectIdException.class, () -> postsServiceImp.getAllByUserId("abc"));
    }

    @Test
    @DisplayName("Test getAllPostsFromFriends")
    void testGetAllPostsFromFriends() throws IncorrectIdException {
        List<Friends> friendsList = new ArrayList<>();
        Friends friend = new Friends();
        friend.setUserTo(2);
        friendsList.add(friend);
        when(friendsRepo.getAllFriends(anyInt())).thenReturn(friendsList);
        List<Integer> friendsIdList = new ArrayList<>();
        friendsIdList.add(2);
        when(entityManagerFactory.createEntityManager()).thenReturn(mock(EntityManager.class));
        Query query = mock(Query.class);
        when(query.setParameter(anyString(), anyList())).thenReturn(query);
        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        when(entityManagerFactory.createEntityManager().createQuery(anyString())).thenReturn(query);
        List<Posts> result = postsServiceImp.getAllPostsFromFriends("1", 1, 10);
        Assertions.assertEquals(new ArrayList<>(), result);
    }

    @Test
    @DisplayName("Test savePost")
    void testSavePost() {
        Posts post = new Posts();
        postsServiceImp.savePost(post);
        verify(postsRepo, times(1)).save(post);
    }

    @Test
    @DisplayName("Test editPost")
    void testEditPost() {
        postsServiceImp.editPost("header", "text", "pic", "1");
        verify(postsRepo, times(1)).editPost("header", "text", "pic", "1");
    }

    @Test
    @DisplayName("Test getPic when post is present")
    void testGetPicWhenPostIsPresent() throws AttributeNotFoundException, IncorrectIdException {
        Posts post = new Posts();
        post.setPic("pic");
        when(postsRepo.findById(anyInt())).thenReturn(Optional.of(post));
        String result = postsServiceImp.getPic("1");
        Assertions.assertEquals("pic", result);
    }

    @Test
    @DisplayName("Test getPic when post is not present")
    void testGetPicWhenPostIsNotPresent() throws AttributeNotFoundException, IncorrectIdException {
        when(postsRepo.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(AttributeNotFoundException.class, () -> postsServiceImp.getPic("1"));
    }

    @Test
    @DisplayName("Test saveImage")
    void testSaveImage() throws IOException {
        MultipartFile pic = new MockMultipartFile("pic", new byte[]{});
        when(postsServiceImp.saveImage(pic)).thenReturn("path/to/file");
        String result = postsServiceImp.saveImage(pic);
        Assertions.assertEquals("path/to/file", result);
    }

    @Test
    @DisplayName("Test deletePost")
    void testDeletePost() {
        postsServiceImp.deletePost(1);
        verify(postsRepo, times(1)).deleteById(1);
    }
}