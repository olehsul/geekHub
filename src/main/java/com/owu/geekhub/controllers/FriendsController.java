package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserFriendDAO;
import com.owu.geekhub.models.FriendStatus;
import com.owu.geekhub.models.UserFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class FriendsController {
    @Autowired
    UserFriendDAO userFriendDAO;

//    @PostMapping("/friend-request-{friendId}-from-{userId}")
    @PostMapping("/friend-request")
    @ResponseBody
    public boolean friendRequest(
            @RequestParam Long friendId,
            @RequestParam Long userId
    ) {
        System.out.println("INSIDE FRIEND REQUEST POSTMAPPING");
        try {
            UserFriend userFriend = UserFriend.builder()
                    .userId(userId)
                    .friendId(friendId)
                    .status(FriendStatus.PENDING)
                    .build();


            userFriendDAO.save(userFriend);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
