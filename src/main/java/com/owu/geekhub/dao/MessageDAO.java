package com.owu.geekhub.dao;

import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDAO extends JpaRepository<Message, Long> {
    Message findBySender(User sender);
}
