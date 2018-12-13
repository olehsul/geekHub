package com.owu.geekhub.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Builder
@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="msg_id", unique = true, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONVERSE_ID")
    private Conversation conversation;
    @ManyToOne(fetch = FetchType.LAZY,
    targetEntity = User.class)
    private User sender;
    private String content;
    private Date createDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id", referencedColumnName = "msg_id")
    private Message parentMessage;
    
}
