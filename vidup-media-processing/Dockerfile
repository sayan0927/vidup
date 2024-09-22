# Use an official OpenJDK runtime as a parent image
FROM openjdk:22-jdk-slim

# Set the working directory in the container
WORKDIR /media_procc


RUN apt-get -y update && apt-get -y upgrade && apt-get install -y ffmpeg


# Copy the project jar file to the workdir
COPY target/vidup-media-processing-0.0.1-SNAPSHOT.jar media_procc.jar

# Make port 8000 available to the world outside this container
EXPOSE 8001

# Set environment variables
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod


# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar media_procc.jar"]





