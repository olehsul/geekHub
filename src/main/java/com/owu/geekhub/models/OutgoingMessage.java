package com.owu.geekhub.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutgoingMessage {
    private String content;
    private Long conversationId;
    private String recipientUsername;
    private String senderUsername;
}
