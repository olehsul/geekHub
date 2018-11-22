package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserFriendDAO;
import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.UserFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendsRestController {
    @Autowired
    UserFriendDAO userFriendDAO;

    //    @GetMapping("/friend-request-id{friendId}-from-id{userId}")
    @PostMapping("/friend-request")
    public UserFriend friendRequest(
            @RequestBody UserFriend userFriend
//            @RequestParam Long friendId,
//            @RequestParam Long userId
    ) {
//        UserFriend userFriend = new UserFriend(new Long(12), new Long(13));
        System.out.println("INSIDE FRIEND REQUEST POSTMAPPING");
        userFriend.setStatus(FriendStatus.PENDING);
//        UserFriend userFriend = UserFriend.builder()
//                .userId(userId)
//                .friendId(friendId)
//                .status(FriendStatus.PENDING)
//                .build();
        try {

            userFriendDAO.save(userFriend);
        } catch (Exception e) {
            e.printStackTrace();
//            return "redirect:/id" + friendId;
            return null;
        }
        return userFriend;
//        return "redirect:/id" + friendId;
    }


    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(
            @RequestBody Long friendId
    ){
        System.out.println(friendId);
    }

}
