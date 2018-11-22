package com.owu.geekhub.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
public class UserFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long friendId;
    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    public UserFriend(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

}
