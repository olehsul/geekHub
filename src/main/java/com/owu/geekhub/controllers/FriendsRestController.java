package com.owu.geekhub.controllers;

import com.owu.geekhub.models.FriendshipRequest;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class FriendsRestController {

    @Autowired
    private UserFriendService userFriendService;

    @PostMapping("/send-friend-request")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PM')")
    public boolean friendRequest(
            @RequestParam Long friendId
    ) {
        log.debug("Sending friend request to user@id:" + friendId + "...");
        try {
            userFriendService.sendFriendRequest(friendId);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }


    @PostMapping("/accept-friend-request")
    public void acceptFriendRequest(
            @RequestParam Long friendId
    ) {
        log.debug("Accepting request from user@id:" + friendId + "...");
        userFriendService.acceptFriendRequest(friendId);
    }

    @PostMapping("/cancel-friend-request")
    public void cancelFriendRequest(
            @RequestParam Long friendId
    ) {
        log.debug("Deleting request to user@id:" + friendId + "...");
        userFriendService.cancelFriendRequest(friendId);
    }

    @PostMapping("/delete-friend")
    public void deleteFriend(
            @RequestParam Long friendId
    ) {
        log.debug("Deleting user@id:" + friendId + " from friends list...");
        userFriendService.deleteFriend(friendId);
    }


    @GetMapping("/incoming-friend-requests")
    public List<FriendshipRequest> getIncomingFriendRequests() {
        log.debug("Getting incoming friend requests...");
        return userFriendService.getIncomingFriendRequests();
    }

    @GetMapping("/outgoing-friend-requests")
    public List<FriendshipRequest> getOutGoingFriendRequests() {
        log.debug("Getting outgoing friend requests...");
        return userFriendService.getOutgoingFriendRequests();
    }
}
