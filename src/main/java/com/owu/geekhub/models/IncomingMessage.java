package com.owu.geekhub.models;

import lombok.Data;

@Data
public class IncomingMessage {
    private String content;
    private Long senderId;
    private Long conversationId;
    private Long recipientId;
}
