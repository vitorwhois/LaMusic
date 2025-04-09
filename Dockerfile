# Build stage
FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Variáveis de ambiente padrão
ENV SERVER_PORT=8080
ENV SERVER_ADDRESS=0.0.0.0
ENV DDL_AUTO=validate
ENV SHOW_SQL=false

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]