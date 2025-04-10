$services = docker compose config --services
foreach ($service in $services) {
    Write-Host "Baslatiliyor: $service"
    docker compose up -d $service
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Hata: $service baslatilamiyor"
    } else {
        Write-Host "Basarili: $service calisiyor"
    }
}
