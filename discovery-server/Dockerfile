FROM maven:3.9.6-eclipse-temurin-21 as build

WORKDIR /app
COPY . /app/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
COPY --from=build /app/target/discovery-server-0.0.1-SNAPSHOT.jar /app/discovery-server.jar

ENTRYPOINT ["java", "-jar", "/app/discovery-server.jar"]