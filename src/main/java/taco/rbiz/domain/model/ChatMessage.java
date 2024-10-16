package taco.rbiz.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Chat Message
 */
@Data
public class ChatMessage {

    private String sender;
    private String message;
    private LocalDateTime timeStamp;

    public ChatMessage() {

    }

    public ChatMessage(String sender, String message, LocalDateTime timeStamp) {
        this.sender = sender;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
