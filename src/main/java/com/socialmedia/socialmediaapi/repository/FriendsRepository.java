package com.socialmedia.socialmediaapi.repository;

import com.socialmedia.socialmediaapi.models.Friends;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendsRepository extends JpaRepository<Friends, Integer> {

    @Transactional
    @Query("select friends from Friends friends where friends.userFrom =:userId and friends.userTo = :userId and friends.status = 1")
    List<Friends> getAllFriends(int userId);

    @Transactional
    @Query("select friends from Friends friends where friends.userFrom =:userId")
    List<Friends> getAllSubscribes(int userId);

    @Transactional
    @Modifying
    @Query("update Friends e set e.status=1 where e.userFrom = :userIdFrom and e.userTo = :userIdTo")
    void acceptFriendRequest(int userIdFrom, int userIdTo);

    @Transactional
    @Modifying
    @Query("delete from Friends e where e.userFrom = :userIdFrom and e.userTo = :userIdTo")
    void deleteFromFriends(int userIdFrom, int userIdTo);
    @Transactional
    @Modifying
    @Query("update Friends e set e.status=0 where e.userFrom = :userIdFrom and e.userTo = :userIdTo")
    void changeStatusFromFriendToSubscribe(int userIdFrom, int userIdTo);
}
