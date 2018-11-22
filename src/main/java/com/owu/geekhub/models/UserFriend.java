package com.owu.geekhub.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserFriend {
    @Id
    private Long id;
    private Long userId;
    private Long FriendId;
    private FriendStatus status;
}
