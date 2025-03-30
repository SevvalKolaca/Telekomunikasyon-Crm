# 1. Temel imaj
FROM openjdk:21-jdk-slim

# 2. Uygulama çalışma dizini
WORKDIR /app

# 3. jar dosyasının kopyalanması
# target klasöründe oluşan jar dosyasının ismi
COPY target/user-service.jar user-service.jar

# 4. Uygulama portu (application.properties’te hangi portu dinliyorsak)
EXPOSE 8081

# 5. Container ayağa kalktığında jar'ı çalıştırın
ENTRYPOINT ["java", "-jar", "user-service.jar"]