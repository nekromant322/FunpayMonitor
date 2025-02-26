# 1. Сборка JAR || JDK
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY target/*.jar app.jar

COPY .env .env

ENTRYPOINT ["java", "-jar", "/app/app.jar"]