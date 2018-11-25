package com.owu.geekhub.controllers;

import com.owu.geekhub.models.UserFriend;
import com.owu.geekhub.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class FriendsRestController{

    @Autowired
    private UserFriendService friendService;


    @PostMapping("/friend-request")
    public void friendRequest(
            @RequestBody UserFriend userFriend
    ) {
        System.out.println("INSIDE FRIEND REQUEST POSTMAPPING");
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

}
