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
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER,
            targetEntity = User.class)
    private User sender;
    @ManyToOne(fetch = FetchType.EAGER,
            targetEntity = User.class)
    private User recipient;
    private String content;
    private Date createDate;
    private Date expiryDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id", referencedColumnName = "id")
    private Message parentMessage;
    
}
