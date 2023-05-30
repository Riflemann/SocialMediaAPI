package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.PostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import javax.management.AttributeNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostsServiceImpTest {

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private PostsServiceImp postsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String filePath = "src/main/resources/files";
    }

    @Test
    void getAllByUser_returnsListOfPosts() {
        User user = new User();
        List<Posts> postsList = new ArrayList<>();
        postsList.add(new Posts());
        when(postsRepository.getAllByUserOwner(user)).thenReturn(postsList);

        List<Posts> result = postsService.getAllByUser(Optional.of(user));

        assertEquals(postsList, result);
    }

    @Test
    void getAllByUser_returnsNull() {
        List<Posts> result = postsService.getAllByUser(Optional.empty());

        assertNull(result);
    }

    @Test
    void savePost() {
        Posts post = new Posts();

        postsService.savePost(post);

        verify(postsRepository, times(1)).save(post);
    }

    @Test
    void editPost() {
        String header = "new header";
        String text = "new text";
        String pic = "new pic";
        String id = "1";

        postsService.editPost(header, text, pic, id);

        verify(postsRepository, times(1)).editPost(header, text, pic, id);
    }

    @Test
    void getPic_returnsPic() throws AttributeNotFoundException, IncorrectIdException {
        String id = "1";
        Posts post = new Posts();
        post.setPic("pic");
        when(postsRepository.findById(Integer.parseInt(id))).thenReturn(Optional.of(post));

        String result = postsService.getPic(id);

        assertEquals("pic", result);
    }

    @Test
    void getPic_throwsAttributeNotFoundException() {
        String id = "1";
        when(postsRepository.findById(Integer.parseInt(id))).thenReturn(Optional.empty());

        assertThrows(AttributeNotFoundException.class, () -> postsService.getPic(id));
    }

    @Test
    void getPic_throwsIncorrectIdException() {
        String id = "invalid";

        assertThrows(IncorrectIdException.class, () -> postsService.getPic(id));
    }

    @Test
    void saveImage() throws IOException {
        MockMultipartFile pic = new MockMultipartFile("pic", "pic.jpg", "image/jpeg", "pic".getBytes());
        String pathString = "path/to/pic.jpg";
        File storageFile = new File(pathString);
        Path path = Path.of(pathString);
        when(postsService.saveImage(pic)).thenReturn(pathString);

        String result = postsService.saveImage(pic);

        assertEquals(pathString, result);
        assertTrue(storageFile.exists());
        storageFile.delete();
        Files.deleteIfExists(path);
    }
}