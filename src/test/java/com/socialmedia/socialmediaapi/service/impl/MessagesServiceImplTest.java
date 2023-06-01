package com.socialmedia.socialmediaapi.service.impl;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Messages;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.MessagesRepository;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessagesServiceImplTest {

    @Mock
    private MessagesRepository messagesRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @InjectMocks
    private MessagesServiceImpl messagesService;

    public MessagesServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllMessagesForUser() throws UserNotFoundException {
        int userId = 1;
        User user = User.builder()
                .id(userId)
                .build();
        List<Messages> messagesList = new ArrayList<>();
        messagesList.add(Messages.builder().userTo(userId).build());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(messagesRepository.getAllByUserToIsLike(userId)).thenReturn(messagesList);

        List<Messages> result = messagesService.getAllMessagesForUser(userId);

        assertEquals(messagesList, result);
        verify(messagesRepository).updateMessagesIsRead(messagesList, entityManagerFactory);
    }

    @Test
    public void testGetAllMessagesForUserWithNonExistingUser() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> messagesService.getAllMessagesForUser(userId));
    }

    @Test
    public void testGetAllMessagesFromUser() throws UserNotFoundException {
        int userId = 1;
        User user = User.builder()
                .id(userId)
                .build();
        List<Messages> messagesList = new ArrayList<>();
        messagesList.add(Messages.builder().userFrom(userId).build());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(messagesRepository.getAllByUserFrom(userId)).thenReturn(messagesList);

        List<Messages> result = messagesService.getAllMessagesFromUser(userId);

        assertEquals(messagesList, result);
    }

    @Test
    public void testGetAllMessagesFromUserWithNonExistingUser() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> messagesService.getAllMessagesFromUser(userId));
    }

    @Test
    public void testSendMessage() {
        int userIdFrom = 1;
        int userTo = 2;
        String msg = "Hello";
        Messages message = Messages.builder()
                .txt(msg)
                .userFrom(userIdFrom)
                .userTo(userTo)
                .localDateTimeCreated(LocalDateTime.now())
                .isRead(false)
                .build();

        messagesService.sendMessage(userIdFrom, userTo, msg);

        verify(messagesRepository).save(message);
    }
}