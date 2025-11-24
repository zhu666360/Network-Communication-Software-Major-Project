# SIP 通信系统 - 快速开始指南

> 详细的安装、配置和测试教程

## 🎯 选择你的启动方式

| 方式 | 适用场景 | 启动命令 | 说明 |
|------|---------|---------|------|
| **🎨 GUI 图形界面** ⭐ | 日常使用、完整功能 | `.\start-gui.ps1` | **推荐新用户** |
| **🖥️ 命令行（预配置）** | 快速测试、多用户模拟 | `.\start-sip-user101.ps1` | 开发调试用 |
| **⚙️ 命令行（自定义）** | 高级用户、自动化测试 | `.\run-sip-client.ps1` | 需手动输入配置 |

> 💡 **首次使用？直接跳到 [GUI 快速启动](#gui-快速启动)！**

---

## 目录

- [环境准备](#环境准备)
- [项目编译](#项目编译)
- [GUI 快速启动](#gui-快速启动)
- [命令行启动](#命令行启动)
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

## GUI 快速启动

### 🎨 图形界面客户端（推荐）

**适合人群：** 所有用户，特别是首次使用者

#### 1. 启动 GUI
```powershell
# 方式 1：使用启动脚本（推荐）
.\start-gui.ps1

# 方式 2：使用 Maven
cd sip-client
mvn javafx:run
```

#### 2. 登录界面

GUI 启动后会显示登录界面，默认已填充配置：

```
SIP URI:      sip:101@10.29.133.174:5060
密码:         101
本地 IP:      10.29.133.174
本地端口:     5061
```

**配置说明：**
- **SIP URI**: 你的 SIP 账号（格式：`sip:用户名@服务器地址:端口`）
- **密码**: SIP 账号密码
- **本地 IP**: 本机 IP 地址（用于接收 SIP 消息）
- **本地端口**: 本地监听端口（5060 以上任意端口）

点击 **"登录"** 按钮。

#### 3. 主界面介绍

登录成功后进入主界面：

```
┌─────────────────────────────────────────────────────┐
│  SIP 客户端 - sip:101@10.29.133.174:5060           │
├──────────┬──────────────────────────────────────────┤
│ 联系人   │  [选择联系人开始聊天]  [📞 语音通话]    │
│          │  ────────────────────────────────────── │
│ 👤 102   │                                          │
│          │     💬 聊天消息区域                      │
│ 👤 111   │     （类微信气泡，蓝色=发送，灰色=接收）   │
│          │                                          │
│ 👤 103   │  ────────────────────────────────────── │
│          │  [输入消息...  (Enter发送)]      [发送]  │
├──────────┴──────────────────────────────────────────┤
│  状态栏：已连接                                      │
└─────────────────────────────────────────────────────┘
```

#### 4. 基本操作

**发送消息：**
1. 点击左侧联系人（如 "102"）
2. 在底部输入框输入消息
3. 按 Enter 键发送（Shift+Enter 换行）

**发起通话：**
1. 选择联系人
2. 点击右上角 "📞 语音通话" 按钮
3. 等待对方接听

**接听来电：**
- 有来电时会自动弹出对话框
- 点击 "✓ 接听" 或 "✗ 拒接"

**通话中操作：**
- 显示通话时长
- 点击挂断按钮结束通话

---

## 命令行启动

### 🖥️ 命令行客户端

**适合人群：** 开发者、需要同时启动多个客户端的场景

#### 方式 1：预配置快速启动（推荐）

**启动用户 101：**
```powershell
.\start-sip-user101.ps1
```

**启动用户 102：**
```powershell
.\start-sip-user102.ps1
```

配置已预设好，直接启动即可。

**可用命令：**
```
> help              # 显示帮助
> msg <URI> <内容>  # 发送消息
> call <URI>        # 发起呼叫
> Y / N             # 接听/拒接来电
> hangup <URI>      # 挂断
> unregister        # 注销
> exit              # 退出
```

#### 方式 2：自定义配置启动

```powershell
.\run-sip-client.ps1
```

按提示输入：
- SIP URI（例：`sip:alice@192.168.1.100:5060`）
- 密码
- 本地 IP 和端口

---

## 测试场景

### 场景 1：GUI 客户端 + 命令行客户端混合测试 ⭐

**目的：** 测试 GUI 和命令行之间的消息收发和通话

**推荐理由：** 最直观的测试方式，可以看到 GUI 的完整功能

**步骤：**

#### 1. 启动 GUI 客户端（用户 101）

```powershell
# 终端 1
.\start-gui.ps1
```

- 使用默认配置登录
- 登录成功后看到主界面

#### 2. 启动命令行客户端（用户 102）

```powershell
# 终端 2（新开 PowerShell 窗口）
.\start-sip-user102.ps1
```

#### 3. 测试消息功能

**从 102 发消息给 101：**
```powershell
# 在终端 2 输入
> msg sip:101@10.29.133.174:5060 你好，这是来自102的消息
```

**查看 GUI：**
- GUI 左侧联系人列表应显示 "102"
- 点击 "102" 查看消息内容
- 消息应显示为灰色气泡（接收）

**从 GUI 回复：**
- 选择联系人 "102"
- 输入框输入：`收到，这是来自101的回复`
- 按 Enter 发送

**查看终端 2：**
```
📩 [收到消息] 来自=sip:101@10.29.133.174:5060 内容=收到，这是来自101的回复
```

#### 4. 测试呼叫功能

**方案 A：GUI 呼叫命令行**

1. GUI 中点击 "102" 联系人
2. 点击右上角 "📞 语音通话" 按钮
3. 终端 2 会显示来电提示：
   ```
   ═══════════════════════════════════════
   📞 来电提醒!
      来电方: sip:101@10.29.133.174:5060
   ═══════════════════════════════════════
   是否接听？(Y=接听 / N=拒接): 
   ```
4. 输入 `Y` 接听
5. GUI 显示通话窗口，开始计时
6. GUI 点击挂断按钮结束通话

**方案 B：命令行呼叫 GUI**

1. 终端 2 输入：
   ```powershell
   > call sip:101@10.29.133.174:5060
   ```
2. GUI 弹出来电对话框
3. 点击 "✓ 接听" 或 "✗ 拒接"
4. 接听后 GUI 显示通话窗口
5. 点击挂断或在终端 2 输入：
   ```powershell
   > hangup sip:101@10.29.133.174:5060
   ```

**预期结果：**
- ✅ 消息双向收发正常
- ✅ GUI 显示消息气泡
- ✅ 来电提示正常
- ✅ 接听/拒接功能正常
- ✅ 通话计时正常

---

### 场景 2：两个命令行客户端测试

**目的：** 测试纯命令行环境下的通信

**适合场景：** 快速调试、自动化测试

**步骤：**

#### 1. 启动两个命令行客户端

```powershell
# 终端 1
.\start-sip-user101.ps1
```

```powershell
# 终端 2（新开窗口）
.\start-sip-user102.ps1
```

#### 2. 测试消息收发

**从 101 发给 102：**
```powershell
# 终端 1
> msg sip:102@10.29.133.174:5060 你好，102
```

**终端 2 应显示：**
```
📩 [收到消息] 来自=sip:101@10.29.133.174:5060 内容=你好，102
```

**从 102 回复 101：**
```powershell
# 终端 2
> msg sip:101@10.29.133.174:5060 收到，101
```

#### 3. 测试呼叫流程

**101 呼叫 102：**
```powershell
# 终端 1
> call sip:102@10.29.133.174:5060
```

**终端 2 显示来电：**
```
═══════════════════════════════════════
📞 来电提醒!
   来电方: sip:101@10.29.133.174:5060
═══════════════════════════════════════
是否接听？(Y=接听 / N=拒接): 
```

**输入 Y 接听：**
```powershell
> Y
✓ 已接听来自 sip:101@10.29.133.174:5060 的呼叫
```

**挂断：**
```powershell
# 终端 1
> hangup sip:102@10.29.133.174:5060
```

**预期结果：**
- ✅ 消息双向收发正常
- ✅ 来电提示正常
- ✅ Y/N 接听/拒接正常
- ✅ 挂断正常

**注意：** 当前版本只实现了 SIP 信令，没有真实音频流。

---

### 场景 3：业务服务器测试

**目的：** 测试 Spring Boot REST API 功能

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

**注意：** 当前客户端（GUI 和命令行）**尚未集成**服务器 API，只是测试服务器功能是否正常。

---

## 故障排查

### 问题 1：编译失败

**现象：**
```
[ERROR] Failed to execute goal ... compilation failure
```

**可能原因：**
- JDK 版本不是 17+
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

### 问题 2：GUI 无法启动

**现象：**
```
Error: JavaFX runtime components are missing
或
加载 FXML 失败
```

**解决方案：**

1. **JavaFX 依赖问题**
   ```powershell
   # 确保使用正确的启动脚本
   .\start-gui.ps1
   
   # 不要直接使用 java -jar，会缺少 JavaFX 模块
   ```

2. **FXML 文件路径错误**
   - 检查 `src/main/resources/fxml/` 目录下是否有 FXML 文件
   - 重新编译：`mvn clean install -DskipTests`

3. **CSS 样式加载失败**
   - 检查 `src/main/resources/css/styles.css` 是否存在
   - 查看控制台是否有资源加载错误

---

### 问题 3：SIP 注册超时

**现象：**
```
SIP 注册超时，请检查网络和服务器配置
```

**可能原因：**
1. SIP 服务器未启动或不可达（默认 10.29.133.174:5060）
2. 网络不通或防火墙阻止
3. SIP URI 或密码错误
4. 本地端口被占用

**解决方案：**

1. **检查 SIP 服务器连接**
   ```powershell
   # 测试连接
   Test-NetConnection 10.29.133.174 -Port 5060
   ```

2. **检查防火墙**
   ```powershell
   # Windows 防火墙放行 SIP 端口
   New-NetFirewallRule -DisplayName "SIP Client UDP 5070" -Direction Inbound -Protocol UDP -LocalPort 5070 -Action Allow
   New-NetFirewallRule -DisplayName "SIP Client UDP 5071" -Direction Inbound -Protocol UDP -LocalPort 5071 -Action Allow
   ```

3. **检查端口占用**
   ```powershell
   # 查看端口是否被占用
   netstat -ano | findstr :5070
   netstat -ano | findstr :5071
   
   # 如果被占用，杀死进程
   taskkill /PID <PID> /F
   ```

4. **查看详细日志**
   - GUI：查看控制台窗口输出
   - CLI：直接显示在终端
   - 检查是否有 401/407 认证错误
   - 检查 Contact 头是否正确

---

### 问题 4：消息发送失败

**现象：**
```
消息发送失败: ...
或
目标用户不在线
```

**可能原因：**
1. 目标用户未在线（GUI 或 CLI）
2. SIP URI 格式错误
3. 网络问题或 SIP 服务器故障

**解决方案：**

1. **检查 SIP URI 格式**
   ```
   正确：101 或 102（GUI 会自动补全）
   正确：sip:102@10.29.133.174:5060（命令行完整格式）
   错误：102@10.29.133.174（缺少 sip: 前缀）
   错误：sip:102@10.29.133.174（缺少端口）
   ```

2. **确认目标用户在线**
   - 先启动接收方（GUI 或 CLI）
   - 等待登录成功提示
   - 再从发送方发送消息

3. **检查日志**
   - 发送方：查看是否有 SIP 错误响应
   - 接收方：查看是否收到 MESSAGE 请求
   - 服务器：检查消息是否转发成功

---

### 问题 5：多客户端端口冲突

**现象：**
```
Address already in use
或
端口被占用
```

**原因：** 同一台机器上启动多个客户端，需要使用不同端口

**解决方案：**

1. **使用预设脚本（自动分配端口）**
   ```powershell
   # 终端 1：101 客户端（GUI 自动使用 5070）
   .\start-gui.ps1
   
   # 终端 2：102 客户端（自动使用 5071）
   .\start-sip-user102.ps1
   ```

2. **查找并杀死占用进程**
   ```powershell
   netstat -ano | findstr :5070
   taskkill /PID <PID> /F
   ```

3. **手动指定端口（命令行模式）**
   - 修改启动脚本中的 `-DlocalPort=` 参数
   - 确保每个客户端使用唯一端口

---

### 问题 6：服务器启动失败（admin-server）

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

### 问题 7：JWT Token 验证失败（admin-server）

**现象：**
```
Token 已过期或无效
```

**说明：** 仅在使用 admin-server 业务服务器时会遇到此问题，当前客户端暂未集成。

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

### 问题 8：Maven 依赖下载慢

**解决方案：**

配置阿里云国内镜像，编辑 `~/.m2/settings.xml`：

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

### SIP 客户端日志（GUI 和 CLI 共享后端）

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
[INFO] MessageHandler - 发送消息到 sip:102@10.29.133.174:5060
[INFO] SipUserAgent - 发送 MESSAGE 请求
[INFO] MessageHandler - 消息发送成功
```

**消息接收：**
```
[INFO] SipUserAgent - 收到 MESSAGE 请求
[INFO] MessageHandler - 收到新消息，来自 sip:101@10.29.133.174:5060
[收到消息] 来自 101: 你好！
```

**呼叫相关：**
```
[INFO] CallManager - 发起呼叫到 sip:102@10.29.133.174:5060
[INFO] CallManager - 收到来电，来自 sip:101@10.29.133.174:5060
[INFO] CallManager - 呼叫已建立
```

---

### 服务器日志（admin-server）

**说明：** 仅在启动 admin-server 时才有这些日志，当前客户端暂未集成业务服务器。

**用户登录：**
```
[INFO] AuthController - 收到登录请求: sip:101@10.29.133.174:5060
[INFO] SipService - 开始注册 SIP 用户: sip:101@10.29.133.174:5060
[INFO] SipService - SIP 用户注册成功: sip:101@10.29.133.174:5060
[INFO] AuthController - 用户登录成功: sip:101@10.29.133.174:5060
```

**消息发送：**
```
[INFO] MessageController - 消息发送成功: sip:101@server -> sip:102@server
```

---

## 下一步开发

### 当前项目状态

✅ **已完成（约 60%）：**
- SIP 协议栈集成（JAIN SIP 1.3.0-91）
- 基本通信功能（注册、消息、呼叫信令）
- JavaFX 桌面 GUI 客户端（登录、主界面、呼叫窗口）
- 命令行客户端（测试/调试工具）
- REST API 业务服务器（JWT 认证、用户管理）

🚧 **待开发（约 40%）：**
1. **RTP 音频传输** - 当前只有 SIP 信令，无实际语音
2. **数据库持久化** - 消息历史、通话记录
3. **文件传输** - 图片、文件分享
4. **群组聊天** - 多人会话
5. **高级功能** - 视频通话、屏幕共享

### 开发路线图

参考项目根目录下的文档：
- `README.md` - 完整架构和功能说明
- `TASK-DISTRIBUTION.md` - 4人团队任务分配（2周开发计划）
- `docs/guidance.md` - 技术指导文档

### 技术栈参考

- **RTP/RTCP**：使用 `jlibrtp` 或 Java Sound API
- **数据库**：H2（内存）或 MySQL/PostgreSQL
- **文件传输**：HTTP multipart 或 WebSocket
- **视频编解码**：JavaCV (FFmpeg/OpenCV wrapper)

---

## 获取帮助

- 📖 查看源代码注释和 JavaDoc
- 📄 阅读 `docs/guidance.md` 技术指导
- 🐛 提交 GitHub Issues 报告问题
- 📚 JAIN SIP 官方文档：https://github.com/RestComm/jain-sip
- 🎨 JavaFX 官方文档：https://openjfx.io/

---

**祝开发顺利！** 🚀
