package com.example.sipclient.gui.model;

import java.time.LocalDateTime;

/**
 * 消息模型
 */
public class Message {
    private String content;
    private boolean fromMe;
    private LocalDateTime timestamp;

    public Message(String content, boolean fromMe, LocalDateTime timestamp) {
        this.content = content;
        this.fromMe = fromMe;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public boolean isFromMe() {
        return fromMe;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
