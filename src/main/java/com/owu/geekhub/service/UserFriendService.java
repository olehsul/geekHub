package com.owu.geekhub.service;

import com.owu.geekhub.models.FriendshipRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserFriendService {
    void sendFriendRequest(Long id);
    void acceptFriendRequest(Long id);
    void deleteFriend(Long id);
    List<FriendshipRequest> getIncomingFriendRequests();
}
