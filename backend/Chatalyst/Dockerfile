FROM maven:3.8.5-openjdk-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./Chatalyst/src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
