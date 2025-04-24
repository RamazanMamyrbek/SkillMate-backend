# === Этап сборки ===
FROM maven:3.9.5-eclipse-temurin-21 AS builder

WORKDIR /app
COPY ../ .
RUN mvn clean package -DskipTests

# === Этап запуска ===
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["sh", "-c", "sleep 20 && java -jar app.jar"]
