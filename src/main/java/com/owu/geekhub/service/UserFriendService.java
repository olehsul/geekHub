package com.owu.geekhub.service;

import com.owu.geekhub.models.UserFriend;
import org.springframework.stereotype.Service;

@Service
public interface UserFriendService {
    void friendRequest(UserFriend userFriend);
    void acceptFriendRequest(Long id);
    void deleteFriend(Long id);
}
