package com.owu.geekhub.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    private List<User> users;
    
}
