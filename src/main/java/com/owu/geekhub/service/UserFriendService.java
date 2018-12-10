package com.owu.geekhub.service;

import org.springframework.stereotype.Service;

@Service
public interface UserFriendService {
    void friendRequest(Long id);
    void acceptFriendRequest(Long id);
    void deleteFriend(Long id);
}
