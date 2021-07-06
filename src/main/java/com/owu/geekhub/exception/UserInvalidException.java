package com.owu.geekhub.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserInvalidException extends GenericGeekhubException {


    List<String> errors = null;

    public UserInvalidException(List<String> errors) {
        this.errors = new ArrayList<>();
    }

    public UserInvalidException(String message) {
        super(message);
        this.errors = new ArrayList<>();
    }
}
