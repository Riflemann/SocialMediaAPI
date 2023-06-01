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
/**
 * <p>Класс MessagesServiceImpl реализует интерфейс MessagesService и предоставляет методы для работы с сообщениями.
 *<p>Поля:
 * <p>- messagesRepo - объект {@link MessagesRepository}, используется для доступа к данным сообщений.
 * <p>- userRepo - объект {@link UserRepository}, используется для доступа к данным пользователей.
 * <p>- entityManagerFactory - объект {@link EntityManagerFactory}, используется для управления жизненным циклом сущностей.
 * <p>Конструктор:
 * <p>MessagesServiceImpl(MessagesRepository messagesRepo, UserRepository userRepo, EntityManagerFactory entityManagerFactory)
 * <p>Методы:
 * <p>- getAllMessagesForUser(int userId) - метод возвращает список всех сообщений отправленные пользователю с указанным идентификатором. Если пользователь не найден, метод выбрасывает исключение {@link UserNotFoundException}. Метод также обновляет статус сообщений на "прочитано".
 * <p>- getAllMessagesFromUser(int userId) - метод возвращает список всех сообщений, отправленных от пользователя с указанным идентификатором. Если пользователь не найден, метод выбрасывает исключение {@link UserNotFoundException}.
 * <p>- sendMessage(int userIdFrom, int userTo, String msg) - метод отправляет сообщение от пользователя с указанным идентификатором (userIdFrom) пользователю с указанным идентификатором (userTo) с указанным текстом сообщения (msg).
 * */
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
            return messagesRepo.getAllByUserFrom(userId);
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
