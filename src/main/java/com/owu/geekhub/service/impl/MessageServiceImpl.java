package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.ConversationDao;
import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserConversationDao;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Conversation;
import com.owu.geekhub.models.Message;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserConversation;
import com.owu.geekhub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserConversationDao userConversationDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void save(Message msg) {

    }

    @Override
    public void createConversationIfNotExists(Long userId, Long friendId) {
        List<UserConversation> allConvByUserId = userConversationDao.findAllByUser_id(userId);
        List<UserConversation> allConvByFriendId = userConversationDao.findAllByUser_id(friendId);
        boolean conversationExists = false;
        for (UserConversation userConversation : allConvByFriendId) {
            for (UserConversation conversation : allConvByUserId) {
                if (userConversation.getConversation_id().equals(conversation.getConversation_id())) {
                    conversationExists = true;
                    break;
                }
            }
        }

        User user = userDao.findById(userId).get();
        User friend = userDao.findById(friendId).get();
        if (!conversationExists) {
            Conversation conversation = new Conversation();
            conversationDao.save(conversation);
            conversation.getUsers().add(user);
            conversation.getUsers().add(friend);
            conversationDao.save(conversation);
        }
    }
}
