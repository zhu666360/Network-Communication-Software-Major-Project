package com.example.sipclient.gui.controller;

import com.example.sipclient.call.CallManager;
import com.example.sipclient.gui.model.Contact;
import com.example.sipclient.sip.SipUserAgent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 通话窗口控制器
 */
public class CallController {

    @FXML private Label contactNameLabel;
    @FXML private Label callStatusLabel;
    @FXML private Label timerLabel;
    @FXML private Button hangupButton;
    @FXML private Button muteButton;

    private Contact contact;
    private SipUserAgent userAgent;
    private CallManager callManager;
    private Timeline timer;
    private int seconds = 0;
    private boolean muted = false;

    public void setCallInfo(Contact contact, SipUserAgent userAgent, CallManager callManager) {
        this.contact = contact;
        this.userAgent = userAgent;
        this.callManager = callManager;
        
        contactNameLabel.setText(contact.getDisplayName());
        callStatusLabel.setText("呼叫中...");
        
        // 启动计时器
        startTimer();
    }

    @FXML
    private void handleHangup() {
        try {
            userAgent.hangup(contact.getSipUri());
            stopTimer();
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMute() {
        muted = !muted;
        muteButton.setText(muted ? "取消静音" : "静音");
        // TODO: 实现静音功能
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds++;
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;
            timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
            
            if (seconds == 1) {
                callStatusLabel.setText("通话中");
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) hangupButton.getScene().getWindow();
        stage.close();
    }
}
