FROM maven:3.9.6-eclipse-temurin-21 as build

WORKDIR /app
COPY . /app/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
COPY --from=build /app/target/user-service.jar /app/user-service.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
