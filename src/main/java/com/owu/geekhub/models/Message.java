package com.owu.geekhub.models;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long conversationId;
    @OneToOne(fetch = FetchType.EAGER,
            targetEntity = User.class)
    private User sender;
    private String content;
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
}
