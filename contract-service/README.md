# Contract Service

Sözleşme yönetimi için mikroservis uygulaması.

## Özellikler

- Sözleşme oluşturma, güncelleme, silme ve listeleme
- Faturalama planı yönetimi
- Sözleşme durumu takibi (aktif, askıda, iptal edilmiş)
- Service discovery entegrasyonu
- API Gateway üzerinden erişim
- OpenAPI dokümantasyonu
- Actuator ile izleme ve yönetim
- Circuit breaker ile hata toleransı

## Teknolojiler

- Java 21
- Spring Boot 3.2.3
- Spring Cloud 2023.0.0
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- SpringDoc OpenAPI

## Başlangıç

1. Gereksinimleri yükleyin:
   - Java 21
   - Maven

2. Projeyi derleyin:
```bash
./mvnw clean install
```

3. Uygulamayı çalıştırın:
```bash
./mvnw spring-boot:run
```

## API Dokümantasyonu

- Swagger UI: http://localhost:{port}/swagger-ui.html
- OpenAPI JSON: http://localhost:{port}/v3/api-docs

## İzleme ve Yönetim

Actuator endpoint'leri:
- Sağlık durumu: http://localhost:{port}/actuator/health
- Metrikler: http://localhost:{port}/actuator/metrics
- Tüm endpoint'ler: http://localhost:{port}/actuator

## Katkıda Bulunma

1. Bu repository'yi fork edin
2. Feature branch'i oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'feat: add some amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun 