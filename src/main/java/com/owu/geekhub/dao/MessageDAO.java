package com.owu.geekhub.dao;

import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MessageDAO extends JpaRepository<Message, Long> {
    Set<Message> findMessagesBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
