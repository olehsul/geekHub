package com.owu.geekhub.controllers;

import com.owu.geekhub.models.UserFriend;
import com.owu.geekhub.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class FriendsRestController{

    @Autowired
    private UserFriendService userFriendService;


    @PostMapping("/friendRequest")
    public void friendRequest(
            @RequestBody Map<String, Long> friendId
    ) {
        System.out.println("INSIDE FRIEND REQUEST POST MAPPING");
        System.out.println(friendId);
        Long id = friendId.get("friendId");
        userFriendService.friendRequest(id);
    }



    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(
            @RequestBody Map<String, Long> friendId
    ) {
        System.out.println(friendId);
        Long id = friendId.get("friendId");
        userFriendService.acceptFriendRequest(id);
    }

    @PostMapping("/deleteFriend")
    public void deleteFriend(
            @RequestBody Map<String, Long> friendId
    ){
        System.out.println("delete friend " + friendId);
        Long id = friendId.get("friendId");
        userFriendService.deleteFriend(id);
    }

}
