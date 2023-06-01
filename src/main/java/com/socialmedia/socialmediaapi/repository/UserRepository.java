package com.socialmedia.socialmediaapi.repository;

import com.socialmedia.socialmediaapi.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    Optional<User> getUserByLogin(String login);

    @Transactional
    List<User> getAllByIdIn(List<Integer> id);
}
