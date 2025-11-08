package com.example.admin.service;

import com.example.admin.entity.CallRecord;
import com.example.admin.repository.InMemoryStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallRecordService {

    private final InMemoryStore store;

    public CallRecordService(InMemoryStore store) {
        this.store = store;
    }

    public List<CallRecord> listCallRecords() {
        return store.calls();
    }
}
