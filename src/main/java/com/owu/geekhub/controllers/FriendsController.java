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
//    @GetMapping("/friend-request-id{friendId}-from-id{userId}")


    @PostMapping("/friend-request")
    @ResponseBody
    public UserFriend friendRequest(
            @RequestBody UserFriend userFriend
//            @PathVariable Long friendId,
//            @PathVariable Long userId
    ) {
        System.out.println("INSIDE FRIEND REQUEST POSTMAPPING");
//        try {
//            userFriend.setStatus(FriendStatus.PENDING);
////            UserFriend userFriend = UserFriend.builder()
////                    .userId(userId)
////                    .friendId(friendId)
////                    .status(FriendStatus.PENDING)
////                    .build();
//
//            userFriendDAO.save(userFriend);
//        } catch (Exception e) {
//            e.printStackTrace();
////            return "redirect:/id" + friendId;
//            return null;
//        }
        return userFriend;
//        return "redirect:/id" + friendId;
    }

}
