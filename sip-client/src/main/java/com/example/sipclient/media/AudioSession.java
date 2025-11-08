package com.example.sipclient.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制台版音频会话，仅打印日志，便于后续替换为真实音频栈。
 */
public class AudioSession implements MediaSession {

    private static final Logger log = LoggerFactory.getLogger(AudioSession.class);

    private boolean running;

    @Override
    public void start() {
        running = true;
        log.info("音频会话启动（示例实现，仅日志）");
    }

    @Override
    public void stop() {
        running = false;
        log.info("音频会话结束");
    }

    public boolean isRunning() {
        return running;
    }
}
