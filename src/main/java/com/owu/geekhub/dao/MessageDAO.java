package com.owu.geekhub.dao;

import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface MessageDAO extends JpaRepository<Message, Long> {
//    Set<Message> findMessagesBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
