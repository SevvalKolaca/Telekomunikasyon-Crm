FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY . /app/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
COPY --from=build /app/target/customer-support-service-0.0.1-SNAPSHOT.jar /app/customer-support-service.jar

ENTRYPOINT ["java", "-jar", "/app/customer-support-service.jar"]