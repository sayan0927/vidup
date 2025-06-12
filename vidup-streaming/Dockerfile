# Use an official OpenJDK runtime as a parent image
FROM openjdk:23-jdk

# Set the working directory in the container
WORKDIR /stream



# Copy the project jar file to the workdir
COPY target/vidup-streaming-0.0.1-SNAPSHOT.jar stream.jar

# Make port 8000 available to the world outside this container
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod


# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar stream.jar"]





