package com.owu.geekhub.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

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
//    @Column(name="msg_id", unique = true, nullable = false)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @ToStringExclude
    private Conversation conversation;
    @OneToOne(fetch = FetchType.EAGER,
            targetEntity = User.class)
    private User sender;
    private String content;
    //    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private ZonedDateTime date;
    @OneToOne(fetch = FetchType.LAZY)
    // TODO: make lazy loading for field
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Message parentMessage;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "message_not_seen_by_users",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @Singular
    private List<User> notSeenByUsers = new ArrayList<>();

    public Message(User sender, String content, ZonedDateTime date, List<User> notSeenByUsers) {
        this.sender = sender;
        this.content = content;
        this.date = date;
        this.notSeenByUsers = notSeenByUsers;
    }

    public Message(Conversation conversation, User sender, String content, ZonedDateTime date, List<User> notSeenByUsers) {
        this.conversation = conversation;
        this.sender = sender;
        this.content = content;
        this.date = date;
        this.notSeenByUsers = notSeenByUsers;
    }

    public Message() {
    }
}
