package com.example.admin.entity;

import java.time.Instant;

public record CallRecord(String id, String caller, String callee, String status, Instant startedAt) {}
