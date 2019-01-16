package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.FriendshipRequestDAO;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.FriendshipRequest;
import com.owu.geekhub.models.FriendshipStatus;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserFriendService;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFriendServiceImpl implements UserFriendService {
    @Autowired
    private FriendshipRequestDAO friendshipRequestDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Override
    public void sendFriendRequest(Long receiverId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<User> userFriends = user.getFriends();
        for (User userFriend : userFriends) {
            if (userFriend.getId().equals(receiverId))
                // TODO: throw exception
                return;
        }

        List<FriendshipRequest> userRequests = friendshipRequestDAO.findAllBySender_Id(user.getId());
        for (FriendshipRequest userRequest : userRequests) {
            if (userRequest.getReceiver().getId().equals(receiverId)) {
                return;
            }
        }

        FriendshipRequest request = new FriendshipRequest();
        request.setReceiver(userDao.findById(receiverId).get());
        request.setSender(user);
        request.setStatus(FriendshipStatus.PENDING);
        friendshipRequestDAO.save(request);
    }

    @Override
    public void acceptFriendRequest(Long senderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        FriendshipRequest request = null;
        List<FriendshipRequest> userIncomingRequests = friendshipRequestDAO.findAllByReceiver_Id(user.getId());
        for (FriendshipRequest userRequest : userIncomingRequests) {
            if (userRequest.getSender().getId().equals(senderId))
                request = userRequest;
        }

        List<User> userFriends = user.getFriends();
        for (User userFriend : userFriends) {
            if (userFriend.getId().equals(senderId))
                // TODO: throw exception
                return;
        }
        if (request != null) {
            user.getFriends().add(request.getSender());
            request.getSender().getFriends().add(user);
            userDao.save(user);
            userDao.save(request.getSender());
            friendshipRequestDAO.deleteById(request.getId());
        } else {
            System.out.println("Request not found!");
        }
    }

    @Override
    public void deleteFriend(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User friend = userDao.findById(id).get();

        List<User> friends = user.getFriends();
        List<User> friendOf = user.getFriendOf();

        friends.removeIf(nextUser -> nextUser.getId().equals(friend.getId()));
        friendOf.removeIf(nextUser -> nextUser.getId().equals(friend.getId()));
        userService.update(user);
    }

    @Override
    public List<FriendshipRequest> getIncomingFriendRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<FriendshipRequest> userRequests = friendshipRequestDAO.findAllByReceiver_Id(user.getId());
        List<FriendshipRequest> requests = userRequests
                .stream()
                .filter(friendshipRequest -> friendshipRequest.getStatus().equals(FriendshipStatus.PENDING))
                .collect(Collectors.toList());
        return requests;
    }
}
