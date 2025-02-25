# 1. Сборка JAR || JDK
FROM eclipse-temurin:21-jdk as builder
WORKDIR /app
# кеширование зависимостей
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY . .
RUN ./mvnw clean package -DskipTests

# 2. Запуск приложения || JRE
FROM eclipse-temurin:21-jre as runtime
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY .env .env
ENTRYPOINT ["java", "-jar", "/app/app.jar"]