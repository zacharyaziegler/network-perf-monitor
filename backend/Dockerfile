# Stage 1: build the Spring Boot app
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy mvnw + wrapper so cached offline deps
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Pre-fetch dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source & package
COPY src src
RUN ./mvnw package -DskipTests -B

# Stage 2: run
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Pull in the fat-jar
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]