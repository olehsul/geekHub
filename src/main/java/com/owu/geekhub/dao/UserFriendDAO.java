package com.owu.geekhub.dao;

import com.owu.geekhub.models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFriendDAO extends JpaRepository<UserFriend, Long> {
}
