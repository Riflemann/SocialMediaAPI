package com.socialmedia.socialmediaapi.repository;

import com.socialmedia.socialmediaapi.models.Friends;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendsRepository extends CrudRepository<Friends, Integer> {

    @Transactional
    @Query("select friends from Friends friends where friends.userFrom =:userId and friends.userTo = :userId and friends.status = 1")
    List<Friends> getAllFriends(int userId);

    @Transactional
    @Query("select friends from Friends friends where friends.userFrom =:userId and friends.userTo = :userId and friends.status = 0")
    List<Friends> getAllSubscribes(int userId);

    @Transactional
    @Modifying
    @Query("update Friends e set e.status=1 where e.userTo = :userIdFrom and e.userTo = :userIdTo")
    void acceptFriendRequest(int userIdFrom, int userIdTo);
}
