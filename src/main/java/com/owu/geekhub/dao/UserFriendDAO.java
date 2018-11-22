package com.owu.geekhub.dao;

import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendDAO extends JpaRepository<UserFriend, Long> {
    List<UserFriend> findAllByFriendIdAndStatus(Long id, FriendStatus status);
}
