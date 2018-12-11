package com.owu.geekhub.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="conversation_id", unique = true, nullable = false)
    private Long id;

    @ManyToMany
    @JoinTable(name = "user_conversation",
            joinColumns={@JoinColumn(name="conversation_id")},
            inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> users = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy="conversation", cascade = CascadeType.ALL)
    private List<Message> messages;
    @OneToOne
    private Message theLastMessage;
}
