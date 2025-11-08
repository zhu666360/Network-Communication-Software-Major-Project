package com.example.sipclient.chat;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Keeps an in-memory transcript for一个聊天会话，方便 CLI 展示最近消息。
 */
public final class ChatSession {

    private final String sessionId;
    private final boolean groupSession;
    private final List<String> participants;
    private final Deque<String> lastMessages = new ArrayDeque<>();

    public ChatSession(String sessionId, boolean groupSession, List<String> participants) {
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        this.groupSession = groupSession;
        this.participants = participants == null ? new ArrayList<>() : new ArrayList<>(participants);
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isGroupSession() {
        return groupSession;
    }

    public List<String> getParticipants() {
        return List.copyOf(participants);
    }

    public void appendMessage(String from, String text) {
        String line = String.format("[%s] %s: %s", Instant.now(), from, text);
        lastMessages.addLast(line);
        while (lastMessages.size() > 20) {
            lastMessages.removeFirst();
        }
    }

    public List<String> dumpRecentMessages() {
        return List.copyOf(lastMessages);
    }
}
