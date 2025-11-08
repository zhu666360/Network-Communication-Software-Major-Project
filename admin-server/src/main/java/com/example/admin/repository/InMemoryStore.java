package com.example.admin.repository;

import com.example.admin.entity.CallRecord;
import com.example.admin.entity.UserSummary;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryStore {

    private final CopyOnWriteArrayList<UserSummary> users = new CopyOnWriteArrayList<>(List.of(
            new UserSummary("alice", "Alice", true),
            new UserSummary("bob", "Bob", false),
            new UserSummary("carol", "Carol", true)
    ));

    private final CopyOnWriteArrayList<CallRecord> calls = new CopyOnWriteArrayList<>(List.of(
            new CallRecord("c-1", "sip:alice@example.com", "sip:bob@example.com", "COMPLETED", Instant.now().minusSeconds(600)),
            new CallRecord("c-2", "sip:carol@example.com", "sip:dan@example.com", "IN_PROGRESS", Instant.now().minusSeconds(120))
    ));

    public List<UserSummary> users() {
        return users;
    }

    public List<CallRecord> calls() {
        return calls;
    }
}
