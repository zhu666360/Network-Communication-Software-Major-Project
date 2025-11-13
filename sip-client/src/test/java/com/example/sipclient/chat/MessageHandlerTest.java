package com.example.sipclient.chat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageHandlerTest {

    @Test
    void recordsIncomingAndOutgoingMessages() {
        List<String> sink = new ArrayList<>();
        MessageHandler handler = new MessageHandler(sink::add);

        handler.handleIncomingMessage("sip:bob@example.com", "hello");
        handler.handleOutgoingMessage("sip:bob@example.com", "hi there");

        assertEquals(2, sink.size());
        assertTrue(sink.get(0).contains("sip:bob@example.com"));
        assertTrue(sink.get(1).contains("sip:bob@example.com"));

        ChatSession session = handler.listSessions().stream()
                .filter(s -> s.getSessionId().equals("sip:bob@example.com"))
                .findFirst()
                .orElseThrow();
        List<String> transcript = session.dumpRecentMessages();
        assertEquals(2, transcript.size());
        assertTrue(transcript.get(0).contains("hello"));
        assertTrue(transcript.get(1).contains("hi there"));
    }

    @Test
    void keepsOnlyLastTwentyMessagesPerSession() {
        MessageHandler handler = new MessageHandler();
        for (int i = 0; i < 25; i++) {
            handler.handleOutgoingMessage("sip:alice@example.com", "msg-" + i);
        }
        ChatSession session = handler.listSessions().stream()
                .filter(s -> s.getSessionId().equals("sip:alice@example.com"))
                .findFirst()
                .orElseThrow();
        List<String> transcript = session.dumpRecentMessages();
        assertEquals(20, transcript.size());
        assertTrue(transcript.get(0).contains("msg-5"));
        assertTrue(transcript.get(transcript.size() - 1).contains("msg-24"));
    }
}
