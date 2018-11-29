package com.owu.geekhub.models;

import lombok.Data;

@Data
public class UserSearchModel {
    private Long id;
    private String lastName;
    private String firstName;

    public UserSearchModel(Long id, String firstName, String lastName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }
}
