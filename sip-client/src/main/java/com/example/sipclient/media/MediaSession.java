package com.example.sipclient.media;

/**
 * 媒体会话抽象，方便后续替换为真实音频/视频实现。
 */
public interface MediaSession {

    void start();

    void stop();
}
