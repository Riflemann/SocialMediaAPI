package com.socialmedia.socialmediaapi.repository;

import com.socialmedia.socialmediaapi.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    Optional<User> getUserByLogin(String login);

    @Transactional
    Stream<User> getAllByIdIsLikeAndIdIsLike(int userOneId, int userTwoId);
}
