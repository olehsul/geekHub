package com.owu.geekhub.dao;

import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserFriendDao extends JpaRepository<UserFriend, Long> {
    List<UserFriend> findAllByFriendIdAndStatus(Long id, FriendStatus status);
    UserFriend findByFriendIdAndUserId(Long friendId, Long userId);
    boolean existsDistinctByFriendIdAndUserId(Long friendId, Long userId);
    boolean existsDistinctByFriendIdAndUserIdAndStatus(Long userId, Long friendId, FriendStatus status);
    @Transactional
    void removeByFriendIdAndUserId(Long friendId, Long userId);
}
