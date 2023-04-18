# Use a Maven image as the build environment
FROM maven:3.8.3-openjdk-11-slim AS build-env

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the application source code to the container
COPY src/ ./src/

# Build the application
RUN mvn package

# Use a Java runtime as the parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application jar file to the container
COPY --from=build-env /app/target/transportcompany.jar /app/transportcompany.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Set the command to run the application when the container starts
CMD ["java", "-jar", "transportcompany.jar"]
