package com.example.sipclient.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * 联系人模型
 */
public class Contact {
    private String userId;
    private String sipUri;
    private String displayName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private int unreadCount;
    private ObservableList<Message> messages;

    public Contact(String userId, String sipUri, String displayName) {
        this.userId = userId;
        this.sipUri = sipUri;
        this.displayName = displayName;
        this.messages = FXCollections.observableArrayList();
        this.unreadCount = 0;
    }

    public String getUserId() {
        return userId;
    }

    public String getSipUri() {
        return sipUri;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void incrementUnreadCount() {
        this.unreadCount++;
    }

    public void clearUnreadCount() {
        this.unreadCount = 0;
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return sipUri.equals(contact.sipUri);
    }

    @Override
    public int hashCode() {
        return sipUri.hashCode();
    }
}
