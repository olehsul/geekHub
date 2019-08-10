package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.ConversationDao;
import com.owu.geekhub.dao.MessageDAO;
import com.owu.geekhub.dao.UserConversationDao;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.*;
import com.owu.geekhub.service.MessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {


    private static final String UPDATE_CONVERSATION_URL = "/chat/update-conversation-for-";
    private static final String READ_MESSAGES_IN_CONVERSATION_URL = "/chat/get-read-messages-in-conversation-";
    private static final String PRIVATE_MESSAGE_URL = "/chat/private-messages-for-conversation-id";
    private final MessageDAO messageDAO;

    private final ConversationDao conversationDao;

    private final UserConversationDao userConversationDao;

    private final UserDao userDao;

    public MessageServiceImpl(MessageDAO messageDAO, ConversationDao conversationDao, UserConversationDao userConversationDao, UserDao userDao) {
        this.messageDAO = messageDAO;
        this.conversationDao = conversationDao;
        this.userConversationDao = userConversationDao;
        this.userDao = userDao;
    }

    @Override
    public void createConversationIfNotExists(Long userId, Long friendId) {
        if (!checkIfConversationExists(userId, friendId)) {
            User user = userDao.findById(userId).get();
            User friend = userDao.findById(friendId).get();
            Conversation conversation = new Conversation();
            conversation.setUsers(new ArrayList<>());
            conversation.getUsers().add(user);
            conversation.getUsers().add(friend);
            conversation.setMessages(new ArrayList<>());
            conversationDao.save(conversation);
        }
    }

    private boolean checkIfConversationExists(Long userId, Long friendId) {
        List<UserConversation> conversationsByUserId = userConversationDao.findAllByUser_id(userId);
        List<UserConversation> conversationsByFriendId = userConversationDao.findAllByUser_id(friendId);
        for (UserConversation userConversation : conversationsByFriendId) {
            for (UserConversation conversation : conversationsByUserId) {
                if (userConversation.getConversation_id().equals(conversation.getConversation_id())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Conversation gotoConversation(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Conversation conversation = null;
        try {
            createConversationIfNotExists(principal.getId(), friendId);
            List<UserConversation> friendConversations = userConversationDao.findAllByUser_id(friendId);
            List<UserConversation> principalConversations = userConversationDao.findAllByUser_id(principal.getId());
            for (UserConversation friendConversation : friendConversations) {
                for (UserConversation principalConversation : principalConversations) {
                    if (friendConversation.getConversation_id().equals(principalConversation.getConversation_id())) {
                        conversation = conversationDao.findById(principalConversation.getConversation_id()).get();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conversation;
    }

    @Override
    public List<Conversation> getConversations(String username) {
        User user = userDao.findByUsername(username);
        List<Conversation> collected = user.getConversations()
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.getTheLastMessage() == null && o2.getTheLastMessage() == null)
                        return 0;
                    else if (o1.getTheLastMessage() == null && o2.getTheLastMessage() != null)
                        return -1;
                    else if (o2.getTheLastMessage() == null && o1.getTheLastMessage() != null)
                        return 1;
                    else
                        return o1.getTheLastMessage().getDate().compareTo(o2.getTheLastMessage().getDate());
                })
                .collect(Collectors.toList());
        Collections.reverse(collected);
        return collected;
    }

    @Override
    public void saveMessageAsRead(Long conversationId, String receiver, SimpMessagingTemplate template) {
        List<Message> readMessages = new ArrayList<>();

        List<Message> unreadMessages = messageDAO
                .findDistinctByUnreadByUsersIn(Collections
                        .singletonList(userDao.findByUsername(receiver)));

        for (Message message : unreadMessages) {
            Iterator<User> iterator = message.getUnreadByUsers().iterator();
            while (iterator.hasNext()) {
                User notSeenByUser = iterator.next();
                if (notSeenByUser.getUsername().equals(receiver)) {
                    readMessages.add(message);
                    iterator.remove();
                }
            }
            messageDAO.save(message);
        }

        if (!readMessages.isEmpty()) {
            template.convertAndSend(READ_MESSAGES_IN_CONVERSATION_URL
                    + conversationId, readMessages);

//            Optional<Conversation> optional = conversationDao.findById(conversationId);
//            optional.ifPresent(conversation -> conversation.getUsers().forEach(user -> {
//                template.convertAndSend(UPDATE_CONVERSATION_URL + user.getUsername(), conversation);
//            }));
        }
    }

    @Override
    public void privateMessage(OutgoingMessage outgoingMessage, SimpMessagingTemplate template) {
        if (outgoingMessage.getContent() == null) {
            return;
        }

        Optional<Conversation> optional = conversationDao.findById(outgoingMessage.getConversationId());
        if (!optional.isPresent()) {
            return;
        }
        Conversation conversation = optional.get();

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal();
        User principal = userDao.findByUsername(outgoingMessage.getSenderUsername());

        Message message = Message.builder()
                .content(outgoingMessage.getContent())
                .sender(principal)
                .date(ZonedDateTime.now())
//                .time(new Time(System.currentTimeMillis()))
                .conversationId(conversation.getId())
                .unreadByUser(userDao.findByUsername(outgoingMessage.getRecipientUsername()))
                .parentMessageId(conversation.getTheLastMessage() != null ? conversation.getTheLastMessage().getId() : null)
                .build();

        messageDAO.save(message);

        conversation.setTheLastMessage(message);
        conversationDao.save(conversation);
        template.convertAndSend(UPDATE_CONVERSATION_URL + principal.getUsername(), conversation);
        template.convertAndSend(UPDATE_CONVERSATION_URL + outgoingMessage.getRecipientUsername(), conversation);
        template.convertAndSend(PRIVATE_MESSAGE_URL + outgoingMessage.getConversationId(), message);
    }
}
