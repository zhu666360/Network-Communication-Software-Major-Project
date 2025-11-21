# SIP 通信系统

> 基于 JAIN SIP + Spring Boot 的企业级即时通信系统  
> 采用 C/S 架构：Java 服务器 + Java PC 客户端

## 项目简介

本项目是一个完整的 SIP 通信系统，实现类似企业 IM 的即时通信功能。系统采用 **三层架构**：

- **SIP 服务器（MSS）** - 负责 SIP 协议处理和路由
- **Java 业务服务器（admin-server）** - 提供用户管理、消息存储、通话记录等业务功能
- **Java PC 客户端（sip-client）** - 桌面客户端，提供用户交互界面

## 系统架构

```
┌─────────────────┐         ┌─────────────────┐
│   Java PC 客户端   │ ←─SIP─→ │   MSS SIP 服务器  │
│  (sip-client)   │         │                 │
└────────┬────────┘         └─────────────────┘
         │
         │ HTTP REST API
         │
         ↓
┌─────────────────┐
│  Java 业务服务器   │
│  (admin-server) │
│  - 用户管理       │
│  - 消息存储       │
│  - 通话记录       │
│  - 统计报表       │
└─────────────────┘
```

## 技术栈

| 模块 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 构建工具 | Maven | 3.x | 多模块项目管理 |
| JDK | OpenJDK | 17 | 运行环境 |
| SIP 协议栈 | JAIN SIP | 1.3.0-91 | SIP 协议实现 |
| SIP 服务器 | MSS (Mobicents) | - | 可选，也可用其他 SIP 服务器 |
| 服务端框架 | Spring Boot | 3.2.5 | REST API 和业务逻辑 |
| 安全认证 | JWT | 0.12.3 | Token 认证 |
| 客户端 UI | Java Swing/JavaFX | - | 桌面 GUI |
| 日志框架 | SLF4J + Logback | 2.0.12 / 1.5.6 | 日志管理 |
| 测试框架 | JUnit 5 + Mockito | 5.10.2 / 5.11.0 | 单元测试 |

## 项目结构

```
Project/
├── pom.xml                           # 父 POM，管理依赖版本
│
├── sip-client/                       # Java PC 客户端模块
│   ├── src/main/java/
│   │   └── com/example/sipclient/
│   │       ├── sip/                  # SIP 协议层
│   │       │   └── SipUserAgent.java # SIP 用户代理
│   │       ├── call/                 # 呼叫管理
│   │       │   ├── CallManager.java
│   │       │   └── CallSession.java
│   │       ├── chat/                 # 消息处理
│   │       │   ├── MessageHandler.java
│   │       │   └── ChatSession.java
│   │       ├── media/                # 媒体会话
│   │       │   └── AudioSession.java
│   │       ├── config/               # 配置管理
│   │       │   └── SipConfig.java
│   │       ├── ui/                   # 用户界面
│   │       │   ├── ConsoleMain.java  # 命令行界面（已实现）
│   │       │   └── gui/              # 图形界面（待实现）
│   │       └── api/                  # 与服务器通信
│   │           └── ServerApiClient.java
│   └── pom.xml
│
├── admin-server/                     # Java 业务服务器模块
│   ├── src/main/java/
│   │   └── com/example/admin/
│   │       ├── controller/           # REST API 控制器
│   │       │   ├── AuthController.java     # 认证接口
│   │       │   ├── UserController.java     # 用户管理
│   │       │   ├── MessageController.java  # 消息管理
│   │       │   ├── CallController.java     # 呼叫管理
│   │       │   └── StatsController.java    # 统计报表
│   │       ├── service/              # 业务逻辑层
│   │       │   ├── SipService.java         # SIP 服务封装
│   │       │   └── UserService.java
│   │       ├── entity/               # 数据模型
│   │       ├── repository/           # 数据访问层
│   │       ├── config/               # 配置类
│   │       │   ├── WebConfig.java          # Web 配置
│   │       │   └── SecurityConfig.java     # 安全配置
│   │       ├── util/                 # 工具类
│   │       │   └── JwtUtil.java            # JWT 工具
│   │       └── dto/                  # 数据传输对象
│   ├── src/main/resources/
│   │   └── application.yml           # 应用配置
│   └── pom.xml
│
├── docs/
│   ├── guidance.md                   # 开发指导文档
│   └── architecture.md               # 系统架构说明
│
├── README.md                         # 本文件
├── QUICKSTART.md                     # 快速开始指南
└── run-sip-client.ps1               # 客户端启动脚本
```

## 核心功能

### ✅ 已完成
- [x] SIP 协议实现（REGISTER/MESSAGE/INVITE/BYE）
- [x] 摘要认证（401/407 Challenge）
- [x] 点对点消息收发
- [x] 音视频呼叫信令（INVITE/ACK/BYE）
- [x] 呼叫状态管理
- [x] 命令行客户端界面
- [x] Spring Boot REST API 服务器
- [x] JWT 认证
- [x] 用户管理接口
- [x] 消息发送接口
- [x] 呼叫控制接口

### 🔄 开发中
- [ ] Java GUI 客户端（Swing/JavaFX）
- [ ] 客户端与服务器 API 集成
- [ ] 消息和通话记录持久化
- [ ] 群聊功能
- [ ] 文件传输

### 📋 待开发
- [ ] 音频采集与播放（Java Sound API）
- [ ] 视频通话
- [ ] NAT 穿透（STUN/TURN）
- [ ] 用户在线状态同步
- [ ] 消息推送通知

---

## 快速开始

### 环境要求

1. **JDK 17**
   ```powershell
   java -version  # 确认输出 "17.x.x"
   ```

2. **Maven 3.x**
   ```powershell
   mvn -v
   ```

3. **MSS SIP 服务器**（可选）
   - 用于真实 SIP 注册测试
   - 或使用其他 SIP 服务器（如 Kamailio、Asterisk）

### 启动步骤

#### 方式 1：快速测试（命令行客户端）

```powershell
# 启动 SIP 客户端
.\run-sip-client.ps1
```

按提示输入：
- SIP URI（例：`sip:alice@192.168.1.100:5060`）
- 密码
- 本地 IP 和端口

**可用命令：**
- `help` - 显示帮助
- `msg <sip:target@host> <内容>` - 发送消息
- `call <sip:target@host>` - 发起呼叫
- `hangup <sip:target@host>` - 挂断
- `unregister` - 注销
- `exit` - 退出

#### 方式 2：完整系统（客户端 + 服务器）

**1. 启动业务服务器**
```powershell
cd admin-server
mvn spring-boot:run
```

服务器运行在 `http://localhost:8081`

**2. 启动客户端**
```powershell
cd sip-client
mvn exec:java "-Dexec.mainClass=com.example.sipclient.ui.ConsoleMain"
```

或使用脚本：
```powershell
.\run-sip-client.ps1
```

**3. 测试 API**
```powershell
# 测试登录
curl -X POST http://localhost:8081/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{
    "sipUri": "sip:alice@192.168.1.100:5060",
    "password": "secret",
    "localIp": "192.168.1.50",
    "localPort": 5070
  }'

# 查看用户列表
curl http://localhost:8081/api/users

# 查看统计数据
curl http://localhost:8081/api/stats
```

---

## 开发指南

### 客户端开发

#### 当前状态
- ✅ 命令行界面（`ConsoleMain.java`）- 已完成
- ❌ 图形界面（GUI）- 待开发

#### GUI 开发计划

**技术选型：**
- **Java Swing** - 轻量、成熟、跨平台
- **JavaFX** - 现代、美观、推荐使用

**推荐界面布局：**
```
┌─────────────────────────────────────────┐
│  SIP 通信客户端                    [最小化][关闭]│
├──────────┬──────────────────────────────┤
│ 联系人列表 │  聊天窗口                      │
│          │  ┌──────────────────────┐    │
│ 👤 Alice  │  │  Alice: 你好！         │    │
│ 👤 Bob    │  │  Me: 你好，在吗？      │    │
│ 👤 Charlie│  │  ...                  │    │
│          │  └──────────────────────┘    │
│          │  ┌──────────────────────┐    │
│          │  │ 输入消息...      [发送] │    │
│ [呼叫] [文件]│  └──────────────────────┘    │
└──────────┴──────────────────────────────┘
```

#### 客户端与服务器通信

客户端需要实现 `ServerApiClient` 类，调用服务器 REST API：

```java
// 登录
POST /api/auth/login
{
  "sipUri": "sip:alice@server",
  "password": "secret",
  "localIp": "192.168.1.50",
  "localPort": 5070
}

// 发送消息（通过服务器记录）
POST /api/messages
Authorization: Bearer {token}
{
  "to": "sip:bob@server",
  "content": "Hello"
}

// 获取消息历史
GET /api/messages/sessions/{sessionId}
Authorization: Bearer {token}

// 获取联系人列表
GET /api/users
Authorization: Bearer {token}
```

### 服务器开发

#### 当前功能
- ✅ JWT 认证
- ✅ SIP 服务封装
- ✅ REST API（用户、消息、呼叫）
- ✅ CORS 配置

#### 待实现功能
1. **数据持久化** - 集成 JPA + MySQL/H2
2. **用户注册** - 新用户注册接口
3. **消息推送** - 实时消息通知（WebSocket 或长轮询）
4. **群聊管理** - 群组创建、成员管理
5. **文件存储** - 文件上传下载接口

#### 数据库设计（建议）

```sql
-- 用户表
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,  -- SIP URI
    display_name VARCHAR(100),
    password VARCHAR(255),
    status VARCHAR(20),           -- ONLINE/OFFLINE/BUSY
    created_at TIMESTAMP
);

-- 消息表
CREATE TABLE messages (
    id VARCHAR(50) PRIMARY KEY,
    from_user VARCHAR(255),
    to_user VARCHAR(255),
    content TEXT,
    timestamp BIGINT,
    status VARCHAR(20)            -- SENT/DELIVERED/READ
);

-- 通话记录表
CREATE TABLE call_records (
    id VARCHAR(50) PRIMARY KEY,
    caller VARCHAR(255),
    callee VARCHAR(255),
    start_time BIGINT,
    end_time BIGINT,
    duration INT,
    status VARCHAR(20)            -- COMPLETED/MISSED/REJECTED
);
```

---

## 测试指南

### 单元测试
```powershell
# 运行所有测试
mvn clean test

# 运行特定模块测试
mvn -pl sip-client test
mvn -pl admin-server test
```

### 集成测试

详见 `QUICKSTART.md` 中的完整测试场景。

**快速测试：**
1. 启动两个客户端实例（不同端口）
2. 在客户端 A：`msg sip:bob@server 你好`
3. 在客户端 B：观察是否收到消息

---

## 常见问题

### Q: 如何实现客户端 GUI？
A: 推荐使用 JavaFX：
1. 在 `sip-client/pom.xml` 添加 JavaFX 依赖
2. 创建 `src/main/java/com/example/sipclient/ui/gui/` 包
3. 实现 `MainWindow.java`（主窗口）、`ChatPanel.java`（聊天面板）等
4. 参考 JavaFX 官方文档：https://openjfx.io/

### Q: 客户端如何保持长连接？
A: 两种方案：
1. **SIP 协议** - 通过 SIP MESSAGE 接收消息（已实现）
2. **WebSocket** - 连接服务器接收推送（需在服务器添加 WebSocket 支持）

### Q: 如何处理离线消息？
A: 
1. 客户端启动时调用 `GET /api/messages/sessions` 获取未读消息
2. 服务器存储离线消息到数据库
3. 用户上线后推送或拉取离线消息

### Q: 多个客户端如何同步状态？
A: 建议实现：
1. 服务器维护用户在线状态
2. 客户端定期心跳（`PUT /api/auth/status`）
3. 使用 WebSocket 推送状态变化

---

## 开发路线图

### 阶段 1：核心功能（当前）
- [x] SIP 协议实现
- [x] 服务器 REST API
- [x] 命令行客户端

### 阶段 2：GUI 客户端（进行中）
- [ ] JavaFX 主窗口
- [ ] 联系人列表
- [ ] 聊天界面
- [ ] 呼叫界面

### 阶段 3：数据持久化
- [ ] 数据库集成
- [ ] 消息存储
- [ ] 通话记录
- [ ] 用户信息管理

### 阶段 4：高级功能
- [ ] 音频采集与播放
- [ ] 群聊功能
- [ ] 文件传输
- [ ] 视频通话

---

## 文档索引

- **`QUICKSTART.md`** - 快速测试指南（详细的测试场景）
- **`docs/guidance.md`** - 软件需求规格说明（SRS）
- **`docs/architecture.md`** - 系统架构设计（待创建）

---

## 团队协作

### Git 工作流
```powershell
# 客户端开发分支
git checkout -b feature/gui-client

# 服务器开发分支
git checkout -b feature/database-integration

# 合并到主分支
git checkout main
git merge feature/gui-client
```

### 代码规范
- 遵循 Java 代码规范（Google Java Style Guide）
- 提交前运行 `mvn clean test` 确保测试通过
- 提交信息格式：`[模块] 简短描述`

---

## 许可证

本项目仅用于课程学习，未设置开源许可证。

---

## 联系方式

项目仓库：https://github.com/huanxu123/sip  
问题反馈：通过 GitHub Issues 提交
