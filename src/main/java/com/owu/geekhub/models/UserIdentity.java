package com.owu.geekhub.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class UserIdentity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    public UserIdentity() {

    }



}