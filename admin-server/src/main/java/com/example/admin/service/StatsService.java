package com.example.admin.service;

import com.example.admin.entity.StatsSummary;
import com.example.admin.entity.UserSummary;
import com.example.admin.repository.InMemoryStore;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final InMemoryStore store;

    public StatsService(InMemoryStore store) {
        this.store = store;
    }

    public StatsSummary snapshot() {
        int total = store.users().size();
        int online = (int) store.users().stream().filter(UserSummary::online).count();
        int activeCalls = (int) store.calls().stream().filter(call -> "IN_PROGRESS".equals(call.status())).count();
        long messagesToday = online * 12L;
        return new StatsSummary(total, online, activeCalls, messagesToday);
    }
}
