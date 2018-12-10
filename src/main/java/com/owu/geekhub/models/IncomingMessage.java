package com.owu.geekhub.models;

import lombok.Data;

@Data
public class IncomingMessage {
    private String content;
    private Long senderId;

    public IncomingMessage(String content, Long senderId) {
        this.content = content;
        this.senderId = senderId;
    }
}
