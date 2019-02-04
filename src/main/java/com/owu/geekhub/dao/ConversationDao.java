package com.owu.geekhub.dao;

import com.owu.geekhub.models.Conversation;
import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationDao extends JpaRepository<Conversation, Long> {
    boolean existsDistinctByUsers(List<User> users);
}
