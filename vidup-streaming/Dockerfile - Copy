# Use an official OpenJDK runtime as a parent image
FROM openjdk:23-jdk

# Set the working directory in the container
WORKDIR /stream

# Copy the entire project into the container
COPY . /stream

# Grant execute permissions to the Maven wrapper
RUN chmod +x mvnw

# Run the Maven build to create the JAR file
RUN ./mvnw clean install

# Make port 8080 available to the outside world
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod

# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/vidup-streaming-0.0.1-SNAPSHOT.jar"]
