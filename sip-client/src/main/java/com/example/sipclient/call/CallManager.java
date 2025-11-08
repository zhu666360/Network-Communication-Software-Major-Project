package com.example.sipclient.call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简化的呼叫管理器，负责记录当前进行中的会话状态，方便后续与媒体层对接。
 */
public class CallManager {

    private static final Logger log = LoggerFactory.getLogger(CallManager.class);

    private final Map<String, CallSession> activeSessions = new ConcurrentHashMap<>();

    public CallSession startOutgoing(String targetUri) {
        CallSession session = new CallSession(targetUri);
        session.markRinging();
        activeSessions.put(session.getId(), session);
        log.info("已发起到 {} 的呼叫，sessionId={}", targetUri, session.getId());
        return session;
    }

    public CallSession acceptIncoming(String fromUri) {
        CallSession session = new CallSession(fromUri);
        session.markActive();
        activeSessions.put(session.getId(), session);
        log.info("自动接听来自 {} 的呼叫，sessionId={}", fromUri, session.getId());
        return session;
    }

    public void terminateByRemote(String remoteUri) {
        findByRemote(remoteUri).ifPresent(session -> {
            session.terminate();
            activeSessions.remove(session.getId());
            log.info("呼叫 {} 已结束", session.getId());
        });
    }

    public Collection<CallSession> listSessions() {
        return activeSessions.values();
    }

    private Optional<CallSession> findByRemote(String remoteUri) {
        return activeSessions.values().stream().filter(s -> s.getRemoteUri().equals(remoteUri)).findFirst();
    }
}
