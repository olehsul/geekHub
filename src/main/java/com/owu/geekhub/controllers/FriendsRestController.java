package com.owu.geekhub.controllers;

import com.owu.geekhub.models.FriendshipRequest;
import com.owu.geekhub.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        System.out.println("Sending friend request to user@id:" + friendId + "...");
        try {
            userFriendService.sendFriendRequest(friendId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @PostMapping("/accept-friend-request")
    public void acceptFriendRequest(
            @RequestParam Long friendId
    ) {
        System.out.println("Accepting request from user@id:" + friendId + "...");
        userFriendService.acceptFriendRequest(friendId);
    }

    @PostMapping("/delete-friend")
    public void deleteFriend(
            @RequestParam Long friendId
    ) {
        System.out.println("Deleting user@id:" + friendId + " friend list...");
        userFriendService.deleteFriend(friendId);
    }

    @GetMapping("/incoming-friend-requests")
    public List<FriendshipRequest> getFriendRequests() {
        System.out.println("Getting friend requests...");
        List<FriendshipRequest> friendRequests = userFriendService.getIncomingFriendRequests();
        for (FriendshipRequest friendRequest : friendRequests) {
            System.out.println(friendRequest);
        }
        return friendRequests;
    }
}
