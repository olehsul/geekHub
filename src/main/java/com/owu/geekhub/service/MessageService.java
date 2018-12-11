package com.owu.geekhub.service;

import com.owu.geekhub.models.Message;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void save(Message msg);
    void createConversationIfNotExists(Long userId, Long friendId);

}
