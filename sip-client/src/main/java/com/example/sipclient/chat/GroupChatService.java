package com.example.sipclient.chat;

import com.example.sipclient.sip.SipUserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.SipException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 群聊管理：维护群成员列表，并复用点对点 MESSAGE 发送能力。
 */
public class GroupChatService {

    private static final Logger log = LoggerFactory.getLogger(GroupChatService.class);

    private final Map<String, ChatSession> groups = new ConcurrentHashMap<>();

    public void defineGroup(String groupId, List<String> members) {
        Objects.requireNonNull(groupId, "groupId");
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("members must not be empty");
        }
        ChatSession session = new ChatSession(groupId, true, new ArrayList<>(members));
        groups.put(groupId, session);
        log.info("群组 {} 初始化，成员数量 {}", groupId, members.size());
    }

    public void broadcastMessage(SipUserAgent userAgent, String groupId, String text) throws SipException {
        ChatSession session = groups.get(groupId);
        if (session == null) {
            throw new IllegalArgumentException("未知群组: " + groupId);
        }
        for (String member : session.getParticipants()) {
            userAgent.sendMessage(member, text);
        }
        session.appendMessage("me", text);
    }

    public void recordIncoming(String groupId, String from, String text) {
        ChatSession session = groups.get(groupId);
        if (session == null) {
            return;
        }
        session.appendMessage(from, text);
    }

    public List<ChatSession> listGroups() {
        return Collections.unmodifiableList(new ArrayList<>(groups.values()));
    }
}
