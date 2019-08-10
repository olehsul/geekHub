package com.owu.geekhub.service;

import com.owu.geekhub.models.Conversation;
import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.OutgoingMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    void createConversationIfNotExists(Long userId, Long friendId);
    Conversation gotoConversation(Long friendId);
    List<Conversation> getConversations(String username);
    void saveMessageAsRead(Long conversationId, String receiver, SimpMessagingTemplate template);
    void privateMessage(OutgoingMessage outgoingMessage, SimpMessagingTemplate template);

}
