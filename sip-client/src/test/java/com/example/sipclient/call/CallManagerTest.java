package com.example.sipclient.call;

import com.example.sipclient.media.MediaSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sip.Dialog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CallManagerTest {

    private static final class StubMediaSession implements MediaSession {
        private boolean started;
        private boolean stopped;

        @Override
        public void start() {
            started = true;
        }

        @Override
        public void stop() {
            stopped = true;
        }
    }

    @Test
    void lifecycleFromOutgoingToTerminate() {
        StubMediaSession mediaSession = new StubMediaSession();
        CallManager manager = new CallManager(() -> mediaSession);

        CallSession session = manager.startOutgoing("sip:bob@example.com");
        assertEquals(CallSession.State.RINGING, session.getState());

        Dialog dialog = Mockito.mock(Dialog.class);
        manager.attachDialog(session.getRemoteUri(), dialog);
        boolean dialogAttached = manager.findByRemote(session.getRemoteUri())
                .map(CallSession::getDialog)
                .map(d -> d == dialog)
                .orElse(false);
        assertTrue(dialogAttached);

        manager.markActive(session.getRemoteUri());
        assertEquals(CallSession.State.ACTIVE, session.getState());
        assertTrue(mediaSession.started);

        manager.terminateLocal(session.getRemoteUri());
        assertEquals(CallSession.State.TERMINATED, session.getState());
        assertTrue(mediaSession.stopped);
        assertTrue(manager.listSessions().isEmpty());
    }

    @Test
    void lifecycleForIncomingCall() {
        CallManager manager = new CallManager();

        CallSession session = manager.acceptIncoming("sip:alice@example.com");
        assertTrue(session.isIncoming());
        assertEquals(1, manager.listSessions().size());

        manager.markActive(session.getRemoteUri());
        assertEquals(CallSession.State.ACTIVE, session.getState());

        manager.terminateByRemote(session.getRemoteUri());
        assertEquals(CallSession.State.TERMINATED, session.getState());
        assertTrue(manager.findByRemote(session.getRemoteUri()).isEmpty());
    }
}
