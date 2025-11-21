# 启动 SIP 客户端 - 用户 102 (测试用)
# MSS 服务器: 10.29.133.174:5060
# SIP 用户: 102

Write-Host ""
Write-Host "=== 启动 SIP 客户端 (用户 102) ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "配置信息:" -ForegroundColor Yellow
Write-Host "  MSS 服务器: 10.29.133.174:5060" -ForegroundColor White
Write-Host "  SIP 用户: 102" -ForegroundColor White
Write-Host "  密码: 102" -ForegroundColor White
Write-Host "  本地 IP: 10.29.133.174" -ForegroundColor White
Write-Host "  本地端口: 5062" -ForegroundColor White
Write-Host ""

# 检查 Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
    Write-Host "✓ Java: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ 未找到 Java，请安装 JDK 17" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "正在启动..." -ForegroundColor Cyan
Write-Host ""

# 切换到项目目录
Set-Location -Path $PSScriptRoot

# 使用预配置的快速启动类
mvn -pl sip-client -q exec:java `
    "-Dexec.mainClass=com.example.sipclient.ui.QuickStartUser102" `
    "-Dexec.cleanupDaemonThreads=false"
