package com.owu.geekhub.models;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Conversation conversation;
    private Long conversationId;
    @OneToOne(fetch = FetchType.EAGER,
            targetEntity = User.class)
    private User sender;
    private String content;
    //    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private ZonedDateTime date;
    private Long parentMessageId;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "message_unread_by_users",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @Singular
    private List<User> unreadByUsers = new ArrayList<>();

    public Message(User sender, String content, ZonedDateTime date, List<User> unreadByUsers) {
        this.sender = sender;
        this.content = content;
        this.date = date;
        this.unreadByUsers = unreadByUsers;
    }

//    public Message(Conversation conversation, User sender, String content, ZonedDateTime date, List<User> unreadByUsers) {
//        this.conversation = conversation;
//        this.sender = sender;
//        this.content = content;
//        this.date = date;
//        this.unreadByUsers = unreadByUsers;
//    }

    public Message(Long conversationId, User sender, String content, ZonedDateTime date, List<User> unreadByUsers) {
        this.conversationId = conversationId;
        this.sender = sender;
        this.content = content;
        this.date = date;
        this.unreadByUsers = unreadByUsers;
    }

    public Message() {
    }
}
