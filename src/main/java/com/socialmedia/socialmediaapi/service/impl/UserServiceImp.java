package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.dto.UserPage;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.FriendsRepository;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import com.socialmedia.socialmediaapi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * <p> Класс UserServiceImp является реализацией интерфейса {@link UserService} и предоставляет методы для работы с пользователями и их друзьями.
 *
 * <p> Поля класса:
 * <p>- userRepo - объект {@link UserRepository}, который используется для доступа к данным пользователей в базе данных
 * <p>- friendsRepo - объект {@link FriendsRepository}, который используется для доступа к данным друзей в базе данных
 *
 *  <p> Конструктор класса:
 * <p>- UserServiceImp(UserRepository userRepo, FriendsRepository friendsRepo)
 *
 * <p> Методы класса:
 * <p>- getAll() - метод, который возвращает список всех пользователей из базы данных
 * <p>- getUserById(int id) - метод, который возвращает пользователя с указанным id. Если пользователь не найден, выбрасывается исключение  {@link UserNotFoundException}
 * <p>- getUserPage(int id) - метод, который возвращает страницу пользователя с указанным id. Если пользователь не найден, выбрасывается исключение {@link UserNotFoundException}
 * <p>- sendRequest(int userIdFrom, int userIdTo) - метод, который отправляет запрос на добавление в друзья от пользователя с id userIdFrom к пользователю с id userIdTo. Если пользователь не найден, выбрасывается исключение {@link UserNotFoundException}UserNotFoundException
 * <p>- getFriendsListById(int userId) - метод, который возвращает список друзей пользователя с указанным id
 * <p>- getSubscribesListById(int userId) - метод, который возвращает список пользователей, на которых подписан пользователь с указанным id
 * <p>- acceptFriendRequest(int userIdFrom, int userIdTo) - метод, который принимает запрос на добавление в друзья от пользователя с id {@value }userIdFrom к пользователю с id userIdTo. Если пользователь не найден, выбрасывается исключение {@link UserNotFoundException}
 * <p>- deleteFromFriends(int userIdFrom, int userIdTo) - метод, который удаляет пользователя с id userIdTo из списка друзей пользователя с id userIdFrom.
 *
 * <p> Приватные методы:
 * <p>- getUserMap(int userIdFrom, int userIdTo) - метод, который возвращает карту пользователей с ключами userIdFrom и userIdTo. Если пользователь не найден, выбрасывается исключение {@link UserNotFoundException}
 * */
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepo;

    private final FriendsRepository friendsRepo;


    public UserServiceImp(UserRepository userRepo, FriendsRepository friendsRepo) {
        this.userRepo = userRepo;
        this.friendsRepo = friendsRepo;
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
    }

    @Override
    public UserPage getUserPage(int id) throws UserNotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return new UserPage(
                    user.get().getFullName(),
                    user.get().getCity(),
                    user.get().getPostsList(),
                    user.get().getFriendsList());
        } else {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
    }

    @Override
    public boolean sendRequest(int userIdFrom, int userIdTo) throws UserNotFoundException {
        Map<Integer, User> userHashMap = getUserMap(userIdFrom, userIdTo);

        Friends friends = Friends.builder()
                .userFrom(userHashMap.get(userIdFrom))
                .userTo(userHashMap.get(userIdTo))
                .status(0)
                .build();
        friendsRepo.save(friends);
        return true;
    }

    @Override
    public List<Friends> getFriendsListById(int userId) {
        return friendsRepo.getAllFriends(userId);
    }

    @Override
    public List<Friends> getSubscribesListById(int userId) {
        return friendsRepo.getAllSubscribes(userId);
    }

    @Override
    public boolean acceptFriendRequest(int userIdFrom, int userIdTo) throws UserNotFoundException {
        friendsRepo.acceptFriendRequest(userIdFrom, userIdTo);

        Map<Integer, User> userHashMap = getUserMap(userIdFrom, userIdTo);

        Friends friends = Friends.builder()
                .userFrom(userHashMap.get(userIdTo))
                .userTo(userHashMap.get(userIdFrom))
                .status(1)
                .build();
        friendsRepo.save(friends);

        return true;
    }

    @Override
    public boolean deleteFromFriends(int userIdFrom, int userIdTo){
        friendsRepo.deleteFromFriends(userIdFrom, userIdTo);
        friendsRepo.changeStatusFromFriendToSubscribe(userIdTo, userIdFrom);
        return true;
    }

    private Map<Integer, User> getUserMap(int userIdFrom, int userIdTo) throws UserNotFoundException {
        Map<Integer, User> userHashMap = new HashMap<>();
        userRepo.getAllByIdIsLikeAndIdIsLike(userIdFrom, userIdTo).forEach(user -> userHashMap.put(user.getId(), user));
        if (userHashMap.containsKey(userIdFrom) && userHashMap.containsKey(userIdTo)) {
            return userHashMap;

        } else {
            throw new UserNotFoundException("Пользователь с ID " + userIdFrom + " или " + userIdTo + " не найден");
        }
    }

}
