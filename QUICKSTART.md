# SIP 通信系统 - 快速开始指南

> 详细的安装、配置和测试教程

## 目录

- [环境准备](#环境准备)
- [项目编译](#项目编译)
- [测试场景](#测试场景)
- [故障排查](#故障排查)

---

## 环境准备

### 1. 安装 JDK 17

**Windows:**
```powershell
# 检查是否已安装
java -version

# 如果未安装，下载 OpenJDK 17
# https://adoptium.net/
```

**验证安装：**
```powershell
PS C:\> java -version
openjdk version "17.0.x" 2024-xx-xx
OpenJDK Runtime Environment (build 17.0.x+x)
OpenJDK 64-Bit Server VM (build 17.0.x+x, mixed mode, sharing)
```

### 2. 安装 Maven

**Windows:**
```powershell
# 检查是否已安装
mvn -v

# 如果未安装，下载 Maven
# https://maven.apache.org/download.cgi
```

**配置环境变量：**
- `MAVEN_HOME` = `C:\apache-maven-3.x.x`
- `Path` 添加 `%MAVEN_HOME%\bin`

**验证安装：**
```powershell
PS C:\> mvn -v
Apache Maven 3.x.x
Maven home: C:\apache-maven-3.x.x
Java version: 17.0.x
```

### 3. 准备 SIP 服务器（可选）

本项目需要一个 SIP 服务器来处理注册和消息路由。

**选项 1：使用 MSS（Mobicents SIP Servlets）**
- 下载：https://github.com/RestComm/sip-servlets
- 配置并启动

**选项 2：使用其他 SIP 服务器**
- Kamailio
- Asterisk
- FreeSWITCH

**选项 3：使用公共测试服务器**
- sip2sip.info
- ekiga.net

**注意：** 没有 SIP 服务器也可以测试客户端界面，但无法完成真实的 SIP 注册。

---

## 项目编译

### 1. 克隆项目
```powershell
git clone https://github.com/huanxu123/sip.git
cd sip/Project
```

### 2. 编译所有模块
```powershell
# 编译整个项目
mvn clean install

# 如果遇到测试失败，可以跳过测试
mvn clean install -DskipTests
```

**编译成功输出：**
```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] project-parent .................................... SUCCESS
[INFO] sip-client ........................................ SUCCESS
[INFO] admin-server ...................................... SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### 3. 运行测试
```powershell
# 运行所有单元测试
mvn test

# 只测试 sip-client 模块
mvn -pl sip-client test

# 只测试 admin-server 模块
mvn -pl admin-server test
```

---

## 测试场景

### 场景 1：命令行客户端快速测试

**目的：** 测试 SIP 协议实现和命令行界面

**步骤：**

1. **启动客户端**
   ```powershell
   .\run-sip-client.ps1
   ```
   
   或手动启动：
   ```powershell
   mvn -pl sip-client exec:java "-Dexec.mainClass=com.example.sipclient.ui.ConsoleMain" "-Dexec.cleanupDaemonThreads=false"
   ```

2. **输入 SIP 配置**
   ```
   请输入 SIP URI（例：sip:alice@192.168.1.100:5060）：
   sip:alice@192.168.1.100:5060
   
   请输入密码：
   secret123
   
   请输入本地 IP（用于接收 SIP 消息）：
   192.168.1.50
   
   请输入本地端口（推荐：5070）：
   5070
   ```

3. **等待注册**
   - 如果有 SIP 服务器，会看到 "SIP 注册成功"
   - 如果没有服务器，会超时（这是正常的）

4. **测试命令**
   ```
   > help
   可用命令：
   - help                           显示帮助信息
   - msg <sip:target@host> <内容>   发送消息
   - call <sip:target@host>         发起呼叫
   - hangup <sip:target@host>       挂断呼叫
   - unregister                     注销
   - exit                           退出程序
   ```

**预期结果：**
- 命令行界面正常显示
- 可以输入命令
- 注册成功（如果有 SIP 服务器）

---

### 场景 2：点对点消息测试

**目的：** 测试两个客户端之间的消息收发

**前置条件：** 
- 需要运行 SIP 服务器
- 准备两个 SIP 账号（如 alice 和 bob）

**步骤：**

1. **启动客户端 A（Alice）**
   ```powershell
   # 终端 1
   .\run-sip-client.ps1
   
   # 输入
   SIP URI: sip:alice@192.168.1.100:5060
   密码: alice123
   本地 IP: 192.168.1.50
   本地端口: 5070
   ```

2. **启动客户端 B（Bob）**
   ```powershell
   # 终端 2（新开一个 PowerShell 窗口）
   cd Project
   mvn -pl sip-client exec:java "-Dexec.mainClass=com.example.sipclient.ui.ConsoleMain" "-Dexec.cleanupDaemonThreads=false"
   
   # 输入
   SIP URI: sip:bob@192.168.1.100:5060
   密码: bob123
   本地 IP: 192.168.1.50
   本地端口: 5080  # 注意：端口要不同
   ```

3. **从 Alice 发送消息给 Bob**
   ```
   # 在客户端 A 的终端中输入
   > msg sip:bob@192.168.1.100:5060 你好，Bob！
   ```

4. **在 Bob 的终端查看**
   ```
   [收到消息] 来自 sip:alice@192.168.1.100:5060: 你好，Bob！
   ```

5. **Bob 回复 Alice**
   ```
   # 在客户端 B 的终端中输入
   > msg sip:alice@192.168.1.100:5060 你好，Alice！我收到了。
   ```

**预期结果：**
- 双方都能成功注册
- Alice 发送的消息被 Bob 接收
- Bob 发送的消息被 Alice 接收
- 消息显示发送者 SIP URI

---

### 场景 3：呼叫测试

**目的：** 测试 SIP INVITE/BYE 呼叫流程

**步骤：**

1. **确保两个客户端都在线**（参考场景 2）

2. **从 Alice 发起呼叫**
   ```
   # 在客户端 A 中输入
   > call sip:bob@192.168.1.100:5060
   正在呼叫 sip:bob@192.168.1.100:5060...
   ```

3. **Bob 会收到来电通知**
   ```
   [来电] 来自 sip:alice@192.168.1.100:5060
   ```

4. **查看呼叫状态**
   - 目前媒体部分为占位实现
   - 只会打印日志，没有真实音频

5. **挂断呼叫**
   ```
   # 在客户端 A 中输入
   > hangup sip:bob@192.168.1.100:5060
   呼叫已挂断
   ```

**预期结果：**
- INVITE 消息成功发送
- Bob 收到来电通知
- 呼叫状态正确转换（IDLE → RINGING → ACTIVE → TERMINATED）
- 挂断后状态恢复

**注意：** 当前版本没有实现真实音频，只是测试 SIP 信令流程。

---

### 场景 4：业务服务器测试

**目的：** 测试 REST API 服务器功能

**步骤：**

1. **启动服务器**
   ```powershell
   cd admin-server
   mvn spring-boot:run
   ```

2. **等待启动完成**
   ```
   Started AdminServerApplication in x.xxx seconds
   ```

3. **测试认证 API**
   ```powershell
   # 测试登录
   curl -X POST http://localhost:8081/api/auth/login `
     -H "Content-Type: application/json" `
     -d '{
       "sipUri": "sip:alice@192.168.1.100:5060",
       "password": "secret123",
       "localIp": "192.168.1.50",
       "localPort": 5070
     }'
   ```

   **预期响应：**
   ```json
   {
     "success": true,
     "data": {
       "token": "eyJhbGciOiJIUzI1NiJ9...",
       "userId": "sip:alice@192.168.1.100:5060",
       "displayName": "alice",
       "expiresIn": 3600
     },
     "message": "登录成功"
   }
   ```

4. **测试用户列表 API**
   ```powershell
   curl http://localhost:8081/api/users
   ```

5. **测试统计 API**
   ```powershell
   curl http://localhost:8081/api/stats
   ```

6. **测试发送消息 API**
   ```powershell
   # 先从登录响应中获取 token
   $token = "eyJhbGciOiJIUzI1NiJ9..."
   
   curl -X POST http://localhost:8081/api/messages `
     -H "Content-Type: application/json" `
     -H "Authorization: Bearer $token" `
     -d '{
       "to": "sip:bob@192.168.1.100:5060",
       "type": "TEXT",
       "content": "通过 API 发送的消息"
     }'
   ```

**预期结果：**
- 服务器成功启动在 8081 端口
- 登录接口返回 JWT Token
- 其他接口正常响应
- 带 Token 的请求认证成功

---

### 场景 5：完整系统集成测试

**目的：** 测试客户端 + 服务器 + SIP 服务器的完整流程

**架构：**
```
客户端 A ──SIP──→ MSS SIP 服务器 ──SIP──→ 客户端 B
    ↓                                      ↓
    └──HTTP──→ 业务服务器 (8081) ←──HTTP──┘
```

**步骤：**

1. **启动 MSS SIP 服务器**（如果有）

2. **启动业务服务器**
   ```powershell
   cd admin-server
   mvn spring-boot:run
   ```

3. **启动客户端 A**
   ```powershell
   # 终端 1
   .\run-sip-client.ps1
   ```

4. **启动客户端 B**
   ```powershell
   # 终端 2
   cd sip-client
   mvn exec:java "-Dexec.mainClass=com.example.sipclient.ui.ConsoleMain" "-Dexec.cleanupDaemonThreads=false"
   ```

5. **测试完整流程**
   - 客户端 A 登录
   - 客户端 B 登录
   - A 发送消息给 B（通过 SIP）
   - B 回复消息（通过 SIP）
   - 查看服务器日志（消息是否被记录）
   - A 发起呼叫
   - B 接听或拒绝

**预期结果：**
- 所有组件正常启动
- SIP 注册成功
- 消息正常收发
- 服务器记录所有活动
- 呼叫流程正常

---

## 故障排查

### 问题 1：编译失败

**现象：**
```
[ERROR] Failed to execute goal ... compilation failure
```

**可能原因：**
- JDK 版本不是 17
- Maven 依赖下载失败

**解决方案：**
```powershell
# 检查 JDK 版本
java -version

# 清理并重新编译
mvn clean
mvn install -U  # -U 强制更新依赖

# 跳过测试编译
mvn clean install -DskipTests
```

---

### 问题 2：SIP 注册超时

**现象：**
```
SIP 注册超时，请检查网络和服务器配置
```

**可能原因：**
1. SIP 服务器未启动
2. 网络不通
3. 防火墙阻止
4. SIP URI 或密码错误
5. 端口被占用

**解决方案：**

1. **检查 SIP 服务器**
   ```powershell
   # 测试连接
   Test-NetConnection 192.168.1.100 -Port 5060
   ```

2. **检查防火墙**
   ```powershell
   # Windows 防火墙放行端口
   New-NetFirewallRule -DisplayName "SIP Client" -Direction Inbound -Protocol UDP -LocalPort 5070 -Action Allow
   ```

3. **检查端口占用**
   ```powershell
   # 查看端口是否被占用
   netstat -ano | findstr :5070
   
   # 如果被占用，更换端口
   ```

4. **使用详细日志**
   - 查看控制台输出的详细错误信息
   - 检查 SIP 请求/响应日志

---

### 问题 3：消息发送失败

**现象：**
```
消息发送失败: ...
```

**可能原因：**
1. 目标用户未在线
2. SIP URI 格式错误
3. 网络问题

**解决方案：**

1. **检查 SIP URI 格式**
   ```
   正确：sip:bob@192.168.1.100:5060
   错误：bob@192.168.1.100
   错误：sip:bob@192.168.1.100（缺少端口）
   ```

2. **确认目标用户在线**
   - 查看服务器用户列表
   - 或在另一个终端启动目标用户客户端

3. **检查日志**
   - 查看发送方日志
   - 查看接收方日志
   - 查看 SIP 服务器日志

---

### 问题 4：端口冲突

**现象：**
```
Address already in use
```

**解决方案：**

1. **查找占用端口的进程**
   ```powershell
   netstat -ano | findstr :5070
   ```

2. **杀死进程**
   ```powershell
   # 假设 PID 是 12345
   taskkill /PID 12345 /F
   ```

3. **使用不同端口**
   - 启动客户端时使用其他端口（如 5080、5090）

---

### 问题 5：服务器启动失败

**现象：**
```
Port 8081 is already in use
```

**解决方案：**

1. **更改服务器端口**
   编辑 `admin-server/src/main/resources/application.yml`：
   ```yaml
   server:
     port: 8082  # 改为其他端口
   ```

2. **或杀死占用 8081 的进程**
   ```powershell
   netstat -ano | findstr :8081
   taskkill /PID <PID> /F
   ```

---

### 问题 6：JWT Token 验证失败

**现象：**
```
Token 已过期或无效
```

**解决方案：**

1. **检查 Token 格式**
   - 必须以 `Bearer ` 开头（注意空格）
   - 例：`Authorization: Bearer eyJhbGc...`

2. **Token 过期**
   - 默认有效期 1 小时
   - 重新登录获取新 Token

3. **检查系统时间**
   - 确保客户端和服务器时间同步

---

### 问题 7：Maven 依赖下载慢

**解决方案：**

配置国内镜像，编辑 `~/.m2/settings.xml`：

```xml
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>Aliyun Maven</name>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
  </mirrors>
</settings>
```

---

## 日志分析

### SIP 客户端日志

**成功注册：**
```
[INFO] SipUserAgent - 正在注册到 SIP 服务器...
[INFO] SipUserAgent - 发送 REGISTER 请求
[INFO] SipUserAgent - 收到 401 Unauthorized，准备认证
[INFO] SipUserAgent - 使用摘要认证重新发送 REGISTER
[INFO] SipUserAgent - 收到 200 OK，注册成功
```

**消息发送：**
```
[INFO] MessageHandler - 发送消息到 sip:bob@server
[INFO] SipUserAgent - 发送 MESSAGE 请求
[INFO] MessageHandler - 消息发送成功
```

**消息接收：**
```
[INFO] SipUserAgent - 收到 MESSAGE 请求
[INFO] MessageHandler - 收到新消息，来自 sip:alice@server
[收到消息] 来自 sip:alice@server: 你好！
```

### 服务器日志

**用户登录：**
```
[INFO] AuthController - 收到登录请求: sip:alice@server
[INFO] SipService - 开始注册 SIP 用户: sip:alice@server
[INFO] SipService - SIP 用户注册成功: sip:alice@server
[INFO] AuthController - 用户登录成功: sip:alice@server
```

**消息发送：**
```
[INFO] MessageController - 消息发送成功: sip:alice@server -> sip:bob@server
```

---

## 性能测试

### 并发用户测试

**目的：** 测试系统支持的并发用户数

**工具：** JMeter 或 Apache Bench

**测试场景：**
1. 100 个用户同时登录
2. 每个用户发送 10 条消息
3. 观察服务器响应时间和资源占用

**性能指标：**
- 平均响应时间 < 100ms
- 99% 请求 < 500ms
- CPU 占用 < 80%
- 内存占用 < 2GB

---

## 下一步

测试完成后，可以开始：

1. **开发 GUI 客户端** - 使用 JavaFX 或 Swing
2. **集成数据库** - 存储消息和通话记录
3. **实现音频** - 使用 Java Sound API
4. **添加群聊** - 扩展消息处理逻辑
5. **优化性能** - 连接池、缓存、异步处理

参考 `README.md` 中的开发路线图。

---

## 获取帮助

- 查看源代码注释
- 阅读 `docs/guidance.md`
- 提交 GitHub Issues
- 查看 JAIN SIP 文档：https://github.com/RestComm/jain-sip

---

**祝测试顺利！** 🚀
