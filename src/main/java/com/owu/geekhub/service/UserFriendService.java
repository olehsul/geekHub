package com.owu.geekhub.service;

import com.owu.geekhub.models.FriendshipRequest;
import com.owu.geekhub.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserFriendService {
    List<User> getFriendsList();
    void sendFriendRequest(Long id);
    void acceptFriendRequest(Long id);
    void cancelFriendRequest(Long id);
    void deleteFriend(Long id);
    List<FriendshipRequest> getIncomingFriendRequests();
    List<FriendshipRequest> getOutgoingFriendRequests();
}
