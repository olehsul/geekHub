package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserFriendDao;
import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.UserFriend;
import com.owu.geekhub.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserFriendServiceImpl implements UserFriendService {

    @Autowired
    private UserFriendDao userFriendDao;

    @Override
    public void friendRequest(UserFriend userFriend) {
        Long friendId = userFriend.getFriendId();
        Long userId = userFriend.getUserId();
        if (!userFriendDao.existsDistinctByFriendIdAndUserId(friendId, userId)){
            userFriend.setStatus(FriendStatus.PENDING);
            try {
                userFriendDao.save(userFriend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acceptFriendRequest(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        UserFriend friendship = userFriendDao.findByFriendIdAndUserId(principal.getId(), id);
        friendship.setStatus(FriendStatus.APPROVED);
        userFriendDao.save(friendship);
        UserFriend duplicate = new UserFriend( friendship.getFriendId(), friendship.getUserId());
        duplicate.setStatus(FriendStatus.APPROVED);
        userFriendDao.save(duplicate);
        System.out.println("=============hello_new_friend===========");
    }
}
