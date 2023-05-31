package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Messages;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.MessagesRepository;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import com.socialmedia.socialmediaapi.service.MessagesService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessagesServiceImpl implements MessagesService {

    private final MessagesRepository messagesRepo;

    private final UserRepository userRepo;

    private final EntityManagerFactory entityManagerFactory;

    public MessagesServiceImpl(MessagesRepository messagesRepo, UserRepository userRepo, EntityManagerFactory entityManagerFactory) {
        this.messagesRepo = messagesRepo;
        this.userRepo = userRepo;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Messages> getAllMessagesForUser(int userId) throws UserNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            List<Messages> messagesList = messagesRepo.getAllByUserToIsLike(userId);
            messagesRepo.updateMessagesIsRead(messagesList, entityManagerFactory);
            return messagesList;
        } else {
            throw new UserNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    @Override
    public List<Messages> getAllMessagesFromUser(int userId) throws UserNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            return messagesRepo.getAllByUserFromIsLike(userId);
        } else {
            throw new UserNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    @Override
    public void sendMessage(int userIdFrom, int userTo, String msg){
        Messages message = Messages.builder()
                .txt(msg)
                .userFrom(userIdFrom)
                .userTo(userTo)
                .localDateTimeCreated(LocalDateTime.now())
                .isRead(false)
                .build();
        messagesRepo.save(message);
    }
}
