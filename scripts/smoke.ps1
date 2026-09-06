$ErrorActionPreference = "Stop"
$base = "http://localhost:8080"

Write-Host "Health gateway..."
Invoke-RestMethod "$base/actuator/health" | Out-Null

Write-Host "Login..."
$body = '{"email":"you@momentum.local","password":"ChangeMeNow!"}'
$tokens = Invoke-RestMethod -Method Post -Uri "$base/api/auth/login" -ContentType "application/json" -Body $body
$headers = @{ Authorization = "Bearer $($tokens.accessToken)" }

Write-Host "Me..."
Invoke-RestMethod -Uri "$base/api/auth/me" -Headers $headers | Out-Null

$today = Get-Date -Format "yyyy-MM-dd"
Write-Host "Planning day..."
Invoke-RestMethod -Uri "$base/api/planning/days?date=$today" -Headers $headers | Out-Null

Write-Host "Stats..."
Invoke-RestMethod -Uri "$base/api/stats/dashboard?period=week" -Headers $headers | Out-Null

Write-Host "Streak..."
Invoke-RestMethod -Uri "$base/api/streak/current" -Headers $headers | Out-Null

Write-Host "OK"
