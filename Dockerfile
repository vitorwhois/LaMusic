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
# Add wait-for-it script
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
ENV SERVER_PORT=8080
ENV SERVER_ADDRESS=0.0.0.0
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["/app/wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]