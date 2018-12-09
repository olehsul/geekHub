package com.owu.geekhub.controllers;

import com.owu.geekhub.models.UserFriend;
import com.owu.geekhub.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class FriendsRestController{

    private final UserFriendService friendService;

    @Autowired
    public FriendsRestController(@Qualifier("userFriendServiceImpl") UserFriendService friendService) {
        this.friendService = friendService;
    }


    @PostMapping("/friend-request")
    public void friendRequest(
            @RequestBody UserFriend userFriend
    ) {
        friendService.friendRequest(userFriend);
    }



    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(
            @RequestBody Map<String, Long> friendId
    ) {
    System.out.println(friendId);
        Long id = friendId.get("friendId");
        friendService.acceptFriendRequest(id);

    }

    @PostMapping("/deleteFriend")
    public void deleteFriend(
            @RequestBody Map<String, Long> friendId
    ){
        System.out.println("delete friend " + friendId);
        Long id = friendId.get("friendId");
        friendService.deleteFriend(id);
    }

}
