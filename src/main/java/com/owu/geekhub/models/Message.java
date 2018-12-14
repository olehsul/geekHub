package com.owu.geekhub.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.sql.Date;

@Builder
@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="msg_id", unique = true, nullable = false)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @ToStringExclude
    private Conversation conversation;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,
    targetEntity = User.class)
    private User sender;
    private String content;
    private Date createDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id", referencedColumnName = "id")
    private Message parentMessage;
    
}
