# -------- BUILD STAGE --------
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw* ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# -------- RUNTIME STAGE --------
FROM openjdk:17-jre-slim


WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the JAR with JVM optimizations
ENTRYPOINT ["java", "-jar", "app.jar"]
