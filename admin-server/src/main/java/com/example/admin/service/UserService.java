package com.example.admin.service;

import com.example.admin.entity.UserSummary;
import com.example.admin.repository.InMemoryStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final InMemoryStore store;

    public UserService(InMemoryStore store) {
        this.store = store;
    }

    public List<UserSummary> listUsers() {
        return store.users();
    }
}
