package com.example.sipclient;

import com.example.sipclient.ui.ConsoleUI;

/**
 * 程序入口：启动控制台 UI。
 */
public final class SipClientApplication {

    private SipClientApplication() {
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}
