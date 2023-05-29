package com.socialmedia.socialmediaapi.repository;

import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostsRepository extends CrudRepository<Posts, Integer> {

    @Transactional
    List<Posts> getAllByUserOwner(User userOwner);

    @Transactional
    @Modifying
    @Query("update Posts e set e.header = :header, e.text = :text, e.pic = :pic where e.id = :id")
    void editPost(String header, String text, String pic, String id);
}
