package com.example.sipclient.call;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents一次点对点呼叫会话的简化状态机。
 */
public final class CallSession {

    public enum State {
        IDLE,
        RINGING,
        ACTIVE,
        TERMINATED
    }

    private final String id;
    private final String remoteUri;
    private final Instant createdAt;
    private State state;

    public CallSession(String remoteUri) {
        this.id = UUID.randomUUID().toString();
        this.remoteUri = Objects.requireNonNull(remoteUri, "remoteUri");
        this.createdAt = Instant.now();
        this.state = State.IDLE;
    }

    public String getId() {
        return id;
    }

    public String getRemoteUri() {
        return remoteUri;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public State getState() {
        return state;
    }

    public void markRinging() {
        this.state = State.RINGING;
    }

    public void markActive() {
        this.state = State.ACTIVE;
    }

    public void terminate() {
        this.state = State.TERMINATED;
    }
}
