package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.dao.UserFriendDao;
import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserFriend;
import com.owu.geekhub.service.UserFriendService;
import com.owu.geekhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserFriendServiceImpl implements UserFriendService {

    @Autowired
    private UserFriendDao userFriendDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Override
    public void addAFriend(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User friend = userDao.findById(id).get();
        User user = userDao.findById(principal.getId()).get();
        System.out.println("you are in add friend " + friend.getId());
        boolean friendIsInList = false;
        List<User> friendList = user.getOutGoingFriendShipRequests();
        for (User user1 : friendList) {
            if (user1.getId().equals(friend.getId())) {
                friendIsInList = true;
                break;
            }
        }
        if (!friendIsInList) {
            user.getOutGoingFriendShipRequests().add(friend);
//            user.getFriendOf().add(friend);
            userService.update(user);
        }
        List<User> friends = user.getIncomingFriendShipRequests();
        for (User f : friends) {
            System.out.println("8888888888888888888888888");
            System.out.println(f);
        }
        List<User> friendOf = user.getOutGoingFriendShipRequests();
        for (User f : friendOf) {
            System.out.println("7777777777777777777777777");
            System.out.println(f);
        }
    }


    @Override
    public void friendRequest(UserFriend userFriend) {
        System.out.println("================you are in method friend request============");
        Long friendId = userFriend.getFriendId();
        Long userId = userFriend.getUserId();


        if (userFriendDao.existsDistinctByFriendIdAndUserId(userId, friendId)) {
            System.out.println("user already Requested");
            return;
        }

        if ((!userFriendDao.existsDistinctByFriendIdAndUserId(friendId, userId))) {
            userFriend.setStatus(FriendStatus.PENDING);
            try {
                userFriendDao.save(userFriend);
                System.out.println("Request is Successful");
            } catch (Exception e) {
                System.out.println("Request error");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acceptFriendRequest(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        UserFriend friendship = userFriendDao.findByFriendIdAndUserId(principal.getId(), id);
        if (!userFriendDao.existsDistinctByFriendIdAndUserIdAndStatus(
                friendship.getUserId(),
                friendship.getFriendId(),
                FriendStatus.APPROVED)) {
            friendship.setStatus(FriendStatus.APPROVED);
            userFriendDao.save(friendship);
            UserFriend duplicate = new UserFriend(friendship.getFriendId(), friendship.getUserId());
            duplicate.setStatus(FriendStatus.APPROVED);
            userFriendDao.save(duplicate);
            System.out.println("=============hello_new_friend===========");
        }
    }

    @Override
    public void deleteFriend(Long id) {
        System.out.println("You are in deleteFriend method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        System.out.println("==========" + id + "========" + principal.getId());
        userFriendDao.removeByFriendIdAndUserId(id, principal.getId());
        userFriendDao.removeByFriendIdAndUserId(principal.getId(), id);
//        userFriendDao.removeUserFriendByFriendIdAndUserId(principal.getId(), id);
//        System.out.println("friend deleted");
    }


}
