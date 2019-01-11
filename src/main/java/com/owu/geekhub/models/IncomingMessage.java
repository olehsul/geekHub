package com.owu.geekhub.models;

import lombok.Data;

@Data
public class IncomingMessage {
    private String content;
    private Long senderId;
    private Long conversationId;
    private Long recipientId;

    public IncomingMessage(String content, Long senderId, Long conversationId, Long recipientId) {
        this.content = content;
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.recipientId = recipientId;
    }

    public IncomingMessage(String content) {
        this.content = content;
    }

    public IncomingMessage(String content, Long senderId) {
        this.content = content;
        this.senderId = senderId;
    }

    public IncomingMessage(String content, Long senderId, Long conversationId) {
        this.content = content;
        this.senderId = senderId;
        this.conversationId = conversationId;
    }
}
