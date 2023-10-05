# Use an official Maven image as a parent image
FROM maven:3.8.1-jdk-11-slim AS build

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application source code to the container
COPY src/ ./src/

# Build the application
RUN mvn package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the application JAR file to the container
COPY --from=build /app/target/transportcompany-0.0.1-SNAPSHOT.jar /app/transportcompany-0.0.1-SNAPSHOT.jar

# Expose port 8080 for the container
EXPOSE 8080

# Run the application when the container starts
CMD ["java", "-jar", "transportcompany-0.0.1-SNAPSHOT.jar"]