# 快速测试运行指南

## 前置准备

1. **确认 JDK 17 已安装**
```powershell
java -version
# 应显示：openjdk version "17.x.x" 或类似
```

2. **确认 Maven 已安装**
```powershell
mvn -v
# 应显示：Apache Maven 3.x.x
```

3. **确认项目已构建**
```powershell
# 在项目根目录执行
mvn clean package
# 应显示 BUILD SUCCESS
```

---

## 测试方法 1：快速启动脚本（推荐）

### Windows PowerShell 脚本

在项目根目录创建并运行以下脚本：

```powershell
# run-sip-client.ps1
Write-Host "=== SIP 客户端快速启动 ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "提示：如果没有真实的 MSS 服务器，可以输入任意值测试界面" -ForegroundColor Yellow
Write-Host ""

mvn -pl sip-client -q exec:java `
  -Dexec.mainClass=com.example.sipclient.ui.ConsoleMain `
  -Dexec.cleanupDaemonThreads=false
```

**使用方法**：
```powershell
# 保存上述内容为 run-sip-client.ps1
.\run-sip-client.ps1
```

---

## 测试方法 2：一行命令启动

```powershell
mvn -pl sip-client -q exec:java -Dexec.mainClass=com.example.sipclient.ui.ConsoleMain -Dexec.cleanupDaemonThreads=false
```

---

## 测试方法 3：模拟场景测试（无需真实 MSS）

### 运行单元测试（验证核心逻辑）

```powershell
# 测试会话管理
mvn -pl sip-client test -Dtest=CallManagerTest

# 测试消息处理
mvn -pl sip-client test -Dtest=MessageHandlerTest

# 运行所有测试
mvn test
```

**预期输出**：
```
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 测试方法 4：在 IDE 中运行

### IntelliJ IDEA / Eclipse

1. 打开项目
2. 找到文件：`sip-client/src/main/java/com/example/sipclient/ui/ConsoleMain.java`
3. 右键 → Run 'ConsoleMain.main()'
4. 在控制台按提示输入参数

---

## 完整交互流程示例

### 启动后的提示与输入

```
=== Project SIP Client - Console ===
SIP URI (e.g. sip:alice@example.com): sip:testuser@192.168.1.100
Password: testpass123
Local IP (reachable by server): 192.168.1.50
Local UDP port (e.g. 5070): 5070

Starting SIP user agent...
Registering (10s timeout)...
Registered: true (state=true)

Commands:
  help                       - show this help
  msg <sip:target> <text>    - send a SIP MESSAGE
  call <sip:target>          - send INVITE
  hangup <sip:target>        - send BYE (if dialog exists)
  unregister                 - unregister and remove contact
  exit                       - exit program
```

### 测试发送消息

```
> msg sip:bob@192.168.1.100 Hello Bob!
Message sent.
```

### 测试发起呼叫

```
> call sip:bob@192.168.1.100
INVITE sent to sip:bob@192.168.1.100
```

### 测试挂断

```
> hangup sip:bob@192.168.1.100
Hangup requested.
```

### 测试注销

```
> unregister
Unregistering...
Unregistered.
```

### 退出程序

```
> exit
Shutting down...
Stopped
```

---

## 测试场景参考

### 场景 1：快速验证编译与启动

**目标**：确认程序能正常启动和交互

```powershell
mvn -pl sip-client exec:java -Dexec.mainClass=com.example.sipclient.ui.ConsoleMain -Dexec.cleanupDaemonThreads=false
```

**输入**（模拟数据，无需真实服务器）：
- SIP URI: `sip:test@localhost`
- Password: `test123`
- Local IP: `127.0.0.1`
- Local port: `5070`

**预期**：程序启动，显示命令提示符 `>`

**验证**：输入 `help` 并回车，应显示命令列表

---

### 场景 2：测试会话管理（单元测试）

**目标**：验证 CallManager 状态迁移逻辑

```powershell
mvn -pl sip-client test -Dtest=CallManagerTest -q
```

**预期输出**：
```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

**日志示例**：
```
INFO com.example.sipclient.call.CallManager -- 已发起到 sip:bob@example.com 的呼叫
INFO com.example.sipclient.call.CallManager -- 呼叫 xxx 已建立媒体通道
INFO com.example.sipclient.call.CallManager -- 呼叫 xxx 已结束
```

---

### 场景 3：测试消息处理（单元测试）

**目标**：验证 MessageHandler 收发与历史记录

```powershell
mvn -pl sip-client test -Dtest=MessageHandlerTest -q
```

**预期**：测试通过，日志显示消息发送与接收记录

---

### 场景 4：与真实 MSS 集成测试（需要 MSS 环境）

#### 前置条件
- MSS 服务器运行在 `192.168.1.100:5060`（示例地址）
- 已创建测试账号：`alice` / `bob`，密码：`password123`
- 本机能访问 MSS（防火墙允许 UDP 5060-5090）

#### 步骤 1：启动客户端 A（alice）

```powershell
mvn -pl sip-client exec:java -Dexec.mainClass=com.example.sipclient.ui.ConsoleMain -Dexec.cleanupDaemonThreads=false
```

**输入**：
- SIP URI: `sip:alice@192.168.1.100`
- Password: `password123`
- Local IP: `192.168.1.50`（本机 IP）
- Local port: `5070`

**预期**：`Registered: true`

#### 步骤 2：启动客户端 B（bob，另一终端）

**输入**：
- SIP URI: `sip:bob@192.168.1.100`
- Password: `password123`
- Local IP: `192.168.1.50`
- Local port: `5071`（不同端口）

**预期**：`Registered: true`

#### 步骤 3：alice 发送消息给 bob

在客户端 A 输入：
```
> msg sip:bob@192.168.1.100 Hi Bob from Alice!
```

在客户端 B 应看到：
```
[INCOMING MESSAGE] from=sip:alice@192.168.1.100 body=Hi Bob from Alice!
```

#### 步骤 4：alice 呼叫 bob

在客户端 A 输入：
```
> call sip:bob@192.168.1.100
```

在客户端 B 应看到：
```
[INCOMING CALL] from=sip:alice@192.168.1.100
```

#### 步骤 5：挂断与注销

```
> hangup sip:bob@192.168.1.100
> unregister
> exit
```

---

## 常见问题排查

### 问题 1：REGISTER 超时或失败

**现象**：
```
Registered: false (state=false)
```

**排查步骤**：

1. 检查 MSS 是否运行：
```powershell
Test-NetConnection -ComputerName 192.168.1.100 -Port 5060
```

2. 检查本地 IP 是否正确（不要用 127.0.0.1）：
```powershell
ipconfig
# 查找本机实际 IP
```

3. 查看详细日志（修改 SipUserAgent.java 中的 TRACE_LEVEL）

---

### 问题 2：端口被占用

**现象**：
```
Exception: Address already in use
```

**解决**：换一个端口（如 5071、5072）

---

### 问题 3：认证失败（401/407 循环）

**现象**：日志显示反复收到 401 Unauthorized

**排查**：
- 确认用户名/密码正确
- 查看 MSS 日志确认账号存在

---

### 问题 4：无日志输出

**现象**：控制台没有任何 INFO/DEBUG 日志

**解决**：项目已添加 logback，应该有日志。若需更详细 SIP 协议日志，修改 SipUserAgent.java：

```java
properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
```

---

## 性能与压力测试（可选）

### 并发注册测试

```powershell
# 创建多个客户端实例（需修改代码或脚本循环）
# 验证 MSS 能否处理 10+ 并发注册
```

### 消息吞吐测试

在 MessageHandlerTest 中增加测试用例：

```java
@Test
void sendHundredMessages() {
    for (int i = 0; i < 100; i++) {
        handler.recordOutgoing("sip:target@test.com", "msg-" + i);
    }
    // 验证性能与内存占用
}
```

---

## 下一步建议

### 已完成（可测试）
- ✅ SIP 注册/注销
- ✅ MESSAGE 发送（信令）
- ✅ INVITE/BYE 信令（占位 SDP）
- ✅ 会话管理（CallManager）
- ✅ 单元测试

### 待完成（需进一步开发）
- ❌ 真实媒体（RTP/音频采集播放）
- ❌ NAT 穿透（STUN/TURN）
- ❌ admin-server 后台
- ❌ JavaFX GUI

---

## 快速参考卡

| 任务 | 命令 |
|------|------|
| 构建项目 | `mvn clean package` |
| 运行客户端 | `mvn -pl sip-client exec:java -Dexec.mainClass=com.example.sipclient.ui.ConsoleMain -Dexec.cleanupDaemonThreads=false` |
| 运行测试 | `mvn test` |
| 单独测试某类 | `mvn -pl sip-client test -Dtest=CallManagerTest` |
| 查看测试报告 | `sip-client/target/surefire-reports/` |
| 清理构建 | `mvn clean` |

---

**文档版本**：1.0  
**更新日期**：2025-11-15
