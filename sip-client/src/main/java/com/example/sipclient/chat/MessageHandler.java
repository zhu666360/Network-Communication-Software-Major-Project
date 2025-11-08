package com.example.sipclient.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Collects incoming/outgoing MESSAGE events，供 UI 展示与群聊服务重复使用。
 */
public class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();
    private final Consumer<String> displaySink;

    public MessageHandler() {
        this(System.out::println);
    }

    public MessageHandler(Consumer<String> displaySink) {
        this.displaySink = displaySink == null ? s -> { } : displaySink;
    }

    public void handleIncomingMessage(String fromUri, String body) {
        ChatSession session = sessions.computeIfAbsent(fromUri, uri -> new ChatSession(uri, false, List.of(uri)));
        session.appendMessage(fromUri, body);
        String line = "收到来自 %s 的消息：%s".formatted(fromUri, body);
        log.info(line);
        displaySink.accept(line);
    }

    public void handleOutgoingMessage(String targetUri, String body) {
        ChatSession session = sessions.computeIfAbsent(targetUri, uri -> new ChatSession(uri, false, List.of(uri)));
        session.appendMessage("me", body);
        String line = "已向 %s 发送消息：%s".formatted(targetUri, body);
        log.info(line);
        displaySink.accept(line);
    }

    public List<ChatSession> listSessions() {
        return List.copyOf(sessions.values());
    }
}
