package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserFriendService;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFriendServiceImpl implements UserFriendService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;


    @Override
    public void friendRequest(Long id) {
        System.out.println("You are in a friendRequest method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User friend = userDao.findById(id).get();
        User user = userDao.findById(principal.getId()).get();
        System.out.println("you are in add friend " + friend.getId());
        boolean friendIsInFriendList = false;
        List<User> friends = user.getFriends();
        for (User user1 : friends) {
            if (user1.getId().equals(friend.getId())) {
                friendIsInFriendList = true;
                break;
            }
        }

        boolean friendIsInRequestList = false;
        List<User> outGoingFriendShipRequestsList = user.getOutGoingFriendShipRequests();
        for (User user1 : outGoingFriendShipRequestsList) {
            if (user1.getId().equals(friend.getId())) {
                friendIsInRequestList = true;
                break;
            }
        }
        List<User> incomingFriendShipRequestsList = user.getIncomingFriendShipRequests();
        for (User user1 : incomingFriendShipRequestsList) {
            if (user1.getId().equals(friend.getId())) {
                friendIsInRequestList = true;
                break;
            }
        }
        if (!friendIsInRequestList&&!friendIsInFriendList) {
            user.getOutGoingFriendShipRequests().add(friend);
//            user.getFriendOf().add(friend);
            userService.update(user);
        }
    }

    @Override
    public void acceptFriendRequest(Long id) {
        System.out.println("You are in a acceptFriendRequest method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User friend = userDao.findById(id).get();
        User user = userDao.findById(principal.getId()).get();
        System.out.println("you are in accept friend " + friend.getId());
        boolean friendIsInFriendList = false;
        List<User> friends = user.getFriends();
        for (User user1 : friends) {
            if (user1.getId().equals(friend.getId())) {
                friendIsInFriendList = true;
                break;
            }
        }
        List<User> friendOf = user.getFriendOf();
        for (User user1 : friendOf) {
            if (user1.getId().equals(friend.getId())) {
                friendIsInFriendList = true;
                break;
            }
        }
        if (!friendIsInFriendList) {
            user.getFriends().add(friend);
            user.getFriendOf().add(friend);
            List<User> incomingFriendShipRequests = user.getIncomingFriendShipRequests();
            incomingFriendShipRequests.removeIf(nextUser -> nextUser.getId().equals(friend.getId()));
            userService.update(user);
        }
    }

    @Override
    public void deleteFriend(Long id) {
        System.out.println("You are in a deleteFriend method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User friend = userDao.findById(id).get();
        User user = userDao.findById(principal.getId()).get();

        List<User> friends = user.getFriends();
        List<User> friendOf = user.getFriendOf();

        friends.removeIf(nextUser -> nextUser.getId().equals(friend.getId()));
        friendOf.removeIf(nextUser -> nextUser.getId().equals(friend.getId()));
        userService.update(user);

    }


}
