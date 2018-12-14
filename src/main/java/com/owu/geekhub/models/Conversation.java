package com.owu.geekhub.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringExclude;

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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_conversation",
            joinColumns={@JoinColumn(name="conversation_id")},
            inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> users = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="conversation", cascade = CascadeType.ALL)
    private List<Message> messages;
    @OneToOne(fetch = FetchType.EAGER)
    private Message theLastMessage;

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                '}';
    }
}
