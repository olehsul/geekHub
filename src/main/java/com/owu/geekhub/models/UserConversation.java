package com.owu.geekhub.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_conversation")
@Data
public class UserConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "userId")
    private Long user_id;

//    @Column(name = "conversationId")
    private Long conversation_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(Long conversation_id) {
        this.conversation_id = conversation_id;
    }

    @Override
    public String toString() {
        return "UserConversation{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", conversation_id=" + conversation_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserConversation that = (UserConversation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user_id, that.user_id) &&
                Objects.equals(conversation_id, that.conversation_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, conversation_id);
    }
}
