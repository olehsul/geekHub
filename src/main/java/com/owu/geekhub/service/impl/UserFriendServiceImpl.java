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
        User sender = (User) authentication.getPrincipal();

        if (checkIfFriends(sender.getId(), receiverId)) {
            System.out.println("Can not send request, because user is already a friend.");
            return;
        }
        if (checkIfHasRequest(sender.getId(), receiverId)) {
            System.out.println("Can not send request, because request was already sent.");
            return;
        }
        if (checkIfHasRequest(sender.getId(), receiverId)) {
            System.out.println("Can not send request, because request was already sent by receiver.");
            return;
        }

        FriendshipRequest request = new FriendshipRequest();
        request.setReceiver(userDao.findById(receiverId).get());
        request.setSender(sender);
        request.setStatus(FriendshipStatus.PENDING);
        friendshipRequestDAO.save(request);
    }

    public void sendFriendRequest(Long receiverId, Long senderId) {
        if (checkIfFriends(senderId, receiverId)) {
            System.out.println("Can not send request, because user is already a friend.");
            // TODO: throw exception
            return;
        }
        if (checkIfHasRequest(senderId, receiverId)) {
            System.out.println("Can not send request, because request was already sent.");
            return;
        }
        if (checkIfHasRequest(senderId, receiverId)) {
            System.out.println("Can not send request, because request was already sent by receiver.");
            return;
        }

        FriendshipRequest request = new FriendshipRequest();
        request.setReceiver(userDao.findById(receiverId).get());
        request.setSender(userDao.findById(senderId).get());
        request.setStatus(FriendshipStatus.PENDING);
        friendshipRequestDAO.save(request);
    }

    @Override
    public void acceptFriendRequest(Long senderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User receiver = (User) authentication.getPrincipal();

        if (checkIfFriends(senderId, receiver.getId())) {
            System.out.println("Can not accept request, because user is already a friend.");
            return;
        }

        FriendshipRequest request = findFriendshipRequest(senderId, receiver.getId());

        if (request != null) {
            receiver.getFriends().add(request.getSender());
            request.getSender().getFriends().add(receiver);
            userDao.save(receiver);
            userDao.save(request.getSender());
            friendshipRequestDAO.deleteById(request.getId());
        }
    }

    @Override
    public void cancelFriendRequest(Long receiverId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sender = (User) authentication.getPrincipal();

        if (checkIfFriends(sender.getId(), receiverId)) {
            System.out.println("Can not cancel request because user is already a friend");
            return;
        }

        FriendshipRequest request = findFriendshipRequest(sender.getId(), receiverId);

        if (request != null) {
            friendshipRequestDAO.deleteById(request.getId());
        }
    }

    @Override
    public List<User> getFriendsList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println(user);
        System.out.println(user.getFriends());
        return user.getFriends();
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
        sendFriendRequest(user.getId(), friend.getId());

        // TODO: fix async actions (remove, accept, cancel)
    }

    @Override
    public List<FriendshipRequest> getIncomingFriendRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return friendshipRequestDAO.findAllByReceiver_Id(principal.getId())
                .stream()
                .filter(friendshipRequest -> friendshipRequest.getStatus().equals(FriendshipStatus.PENDING))
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendshipRequest> getOutgoingFriendRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return friendshipRequestDAO.findAllBySender_Id(principal.getId())
                .stream()
                .filter(friendshipRequest -> friendshipRequest.getStatus().equals(FriendshipStatus.PENDING))
                .collect(Collectors.toList());
    }


    private boolean checkIfFriends(Long senderId, Long receiverId) {
        List<User> userFriends = userDao.findById(senderId).get().getFriends();

        for (User userFriend : userFriends) {
            if (userFriend.getId().equals(receiverId))
                // TODO: throw exception
                return true;
        }
        return false;
    }

    private boolean checkIfHasRequest(Long senderId, Long receiverId) {
        for (FriendshipRequest userRequest : friendshipRequestDAO.findAllBySender_Id(senderId)) {
            if (userRequest.getReceiver().getId().equals(receiverId)) {
                // TODO: throw exception
                return true;
            }
        }
        return false;
    }

    private FriendshipRequest findFriendshipRequest(Long senderId, Long receiverId) {
        List<FriendshipRequest> userIncomingRequests = friendshipRequestDAO.findAllByReceiver_Id(receiverId);
        for (FriendshipRequest userRequest : userIncomingRequests) {
            if (userRequest.getSender().getId().equals(senderId))
                return userRequest;
        }

        System.out.println("Request was not found!");
        // TODO: throw exception
        return null;
    }
}
