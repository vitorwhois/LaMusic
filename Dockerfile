# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV SERVER_PORT=8080
ENV SERVER_ADDRESS=0.0.0.0
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]