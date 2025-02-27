# 1. Сборка JAR || JDK
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY target/*.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Xmx250m", "-jar", "/app/app.jar"]