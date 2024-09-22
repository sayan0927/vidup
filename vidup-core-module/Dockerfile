# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /core


# Copy the project jar file to the workdir
COPY target/vidup-core-module-0.0.1-SNAPSHOT.jar core.jar

# Make port 8000 available to the world outside this container
EXPOSE 8000

# Set environment variables
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod


# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar core.jar"]





