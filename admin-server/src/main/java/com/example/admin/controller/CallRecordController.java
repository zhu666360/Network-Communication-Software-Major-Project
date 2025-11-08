package com.example.admin.controller;

import com.example.admin.entity.CallRecord;
import com.example.admin.service.CallRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calls")
public class CallRecordController {

    private final CallRecordService callRecordService;

    public CallRecordController(CallRecordService callRecordService) {
        this.callRecordService = callRecordService;
    }

    @GetMapping
    public List<CallRecord> listCalls() {
        return callRecordService.listCallRecords();
    }
}
