package com.example.sipclient.ui;

import com.example.sipclient.call.CallManager;
import com.example.sipclient.chat.MessageHandler;
import com.example.sipclient.sip.SipUserAgent;

import java.time.Duration;
import java.util.Scanner;

/**
 * 快速启动脚本 - 用户 102 (用于测试)
 */
public class QuickStartUser102 {

    private static SipUserAgent userAgent;
    private static volatile String pendingCallFrom = null;

    public static void main(String[] args) throws Exception {
        // 预配置参数 - 用户 102
        String uri = "sip:102@10.29.133.174:5060";
        String pwd = "102";
        String localIp = "10.29.133.174";
        int localPort = 5062;  // 不同的端口

        System.out.println("=== Project SIP Client - 用户 102 ===");
        System.out.println("配置:");
        System.out.println("  SIP URI: " + uri);
        System.out.println("  Password: 102");
        System.out.println("  Local IP: " + localIp);
        System.out.println("  Local Port: " + localPort);
        System.out.println();

        System.out.println("正在启动 SIP 用户代理...");
        userAgent = new SipUserAgent(uri, pwd, localIp, localPort);

        // 设置消息处理器
        userAgent.setMessageHandler(new MessageHandler() {
            @Override
            public void handleIncomingMessage(String from, String body) {
                System.out.println("\n📩 [收到消息] 来自=" + from + " 内容=" + body);
                System.out.print("> ");
            }
        });

        CallManager callManager = new CallManager();
        
        // 设置来电监听器
        callManager.setIncomingCallListener((fromUri, sessionId) -> {
            pendingCallFrom = fromUri;
            System.out.println("\n");
            System.out.println("═══════════════════════════════════════");
            System.out.println("📞 来电提醒!");
            System.out.println("   来电方: " + fromUri);
            System.out.println("═══════════════════════════════════════");
            System.out.print("是否接听？(Y=接听 / N=拒接): ");
            System.out.flush();
        });
        
        userAgent.setCallManager(callManager);

        System.out.println("正在注册到 MSS 服务器 (10秒超时)...");
        boolean ok = userAgent.register(Duration.ofSeconds(10));
        System.out.println("注册结果: " + (ok ? "✓ 成功" : "✗ 失败") + " (状态=" + userAgent.isRegistered() + ")");
        System.out.println();

        printHelp();

        Scanner sc = new Scanner(System.in);
        loop:
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line == null) break;
            
            // 处理 Y/N 来电应答
            if (pendingCallFrom != null) {
                String input = line.trim().toUpperCase();
                if (input.equals("Y") || input.equals("YES") || input.equals("是")) {
                    try {
                        userAgent.answerCall(pendingCallFrom);
                        System.out.println("✓ 已接听来自 " + pendingCallFrom + " 的呼叫");
                    } catch (Exception e) {
                        System.err.println("接听失败: " + e.getMessage());
                    }
                    pendingCallFrom = null;
                    continue;
                } else if (input.equals("N") || input.equals("NO") || input.equals("否")) {
                    try {
                        userAgent.rejectCall(pendingCallFrom);
                        System.out.println("✓ 已拒接来自 " + pendingCallFrom + " 的呼叫");
                    } catch (Exception e) {
                        System.err.println("拒接失败: " + e.getMessage());
                    }
                    pendingCallFrom = null;
                    continue;
                }
            }
            
            String[] parts = line.split(" ", 3);
            String cmd = parts[0].trim();
            try {
                switch (cmd) {
                    case "help":
                        printHelp();
                        break;
                    case "msg":
                        if (parts.length < 3) {
                            System.out.println("用法: msg <sip:目标@主机> <消息内容>");
                            break;
                        }
                        userAgent.sendMessage(parts[1].trim(), parts[2]);
                        System.out.println("✓ 消息已发送");
                        break;
                    case "call":
                        if (parts.length < 2) {
                            System.out.println("用法: call <sip:目标@主机>");
                            break;
                        }
                        System.out.println("正在呼叫 " + parts[1].trim() + " ...");
                        userAgent.startCall(parts[1].trim());
                        System.out.println("✓ 呼叫请求已发送");
                        break;
                    case "hangup":
                        if (parts.length < 2) {
                            System.out.println("用法: hangup <sip:目标@主机>");
                            break;
                        }
                        userAgent.hangup(parts[1].trim());
                        System.out.println("✓ 已请求挂断");
                        break;
                    case "unregister":
                        System.out.println("正在注销...");
                        userAgent.unregister(Duration.ofSeconds(5));
                        System.out.println("✓ 已注销");
                        break;
                    case "exit":
                        break loop;
                    default:
                        System.out.println("未知命令。输入 'help' 查看可用命令。");
                }
            } catch (Exception e) {
                System.err.println("命令执行失败: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("\n正在关闭 SIP 客户端...");
        try {
            userAgent.unregister(Duration.ofSeconds(3));
        } catch (Exception e) {
            // ignore
        }
        System.out.println("再见!");
        System.exit(0);
    }

    private static void printHelp() {
        System.out.println("可用命令:");
        System.out.println("  Y / N                             - 接听/拒接来电");
        System.out.println("  help                              - 显示此帮助信息");
        System.out.println("  msg <目标URI> <消息>               - 发送即时消息");
        System.out.println("      示例: msg sip:101@10.29.133.174:5060 你好");
        System.out.println("  call <目标URI>                     - 发起语音通话");
        System.out.println("      示例: call sip:101@10.29.133.174:5060");
        System.out.println("  hangup <目标URI>                   - 挂断通话");
        System.out.println("  unregister                         - 注销登录");
        System.out.println("  exit                               - 退出程序");
        System.out.println();
    }
}
