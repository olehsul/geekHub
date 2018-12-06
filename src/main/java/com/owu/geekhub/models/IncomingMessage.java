package com.owu.geekhub.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class IncomingMessage {
    private String incomingMessage;

    public IncomingMessage(String incomingMessage) {
        this.incomingMessage = incomingMessage;
    }
}
