# Step 1 - Use Java 17 base image to build
FROM eclipse-temurin:17-jdk-alpine AS build

# Step 2 - Set working directory inside container
WORKDIR /app

# Step 3 - Copy maven files first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Step 4 - Download dependencies
RUN ./mvnw dependency:go-offline -B

# Step 5 - Copy source code
COPY src src

# Step 6 - Build the jar file
RUN ./mvnw package -DskipTests

# Step 7 - Use lighter image to run
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Step 8 - Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Step 9 - Expose port
EXPOSE 8081

# Step 10 - Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]