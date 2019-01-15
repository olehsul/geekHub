package com.owu.geekhub.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

}
