# 启动 SIP 客户端 GUI
Write-Host "=== 启动 SIP 客户端 GUI ===" -ForegroundColor Green
Write-Host ""

$PROJECT_ROOT = Split-Path -Parent $MyInvocation.MyCommand.Path

Set-Location "$PROJECT_ROOT\sip-client"

# 检查 Java
$javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_ -replace '.*version "([^"]+)".*', '$1' }
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ 错误: 未找到 Java" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Java: $javaVersion" -ForegroundColor Green
Write-Host ""

Write-Host "正在启动 GUI..." -ForegroundColor Cyan

# 使用 Maven 运行
mvn javafx:run
