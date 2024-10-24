# VIDUP – Video Streaming Application
## Overview

VIDUP is a video streaming web application designed to handle video uploads, real-time streaming, webcam recording, downloading, and a host of user engagement features such as likes/dislikes, comments, playlists, and subscriptions. The application utilizes a hybrid microservice and modulith architecture to provide a scalable, flexible platform that can process videos, detect NSFW content, and manage content efficiently across different services.

The backend is the primary focus of this project, with a proof-of-concept frontend built using Thymeleaf. Though functional, a more robust client-side application is required for production readiness.
Key Features

    Video Uploads: Support for the most common video formats, with MP4 used for storage.
    Real-Time Streaming: Utilizes MPEG-DASH for adaptive bitrate streaming, ensuring smooth playback across varying network conditions and devices.
    Webcam Recording: Users can record videos directly from their browsers.
    User Engagement: Users can interact with content through comments, likes/dislikes, playlists, and subscriptions.
    NSFW Detection: Videos are automatically checked for inappropriate content using a separate Python microservice.
    Statistics and Analytics: Content creators can access performance metrics on their videos.
    Search and Filter: Powerful search functionality with tagging and filtering capabilities.

## Technologies Used

    Backend: Java Spring Boot, FastAPI (Python), MySQL, MongoDB
    Video Processing: FFmpeg 
    Frontend: Thymeleaf,HTML,CSS,JS
    Containerization: Docker 
    Cloud Storage: Azure Files
    Messaging: RabbitMQ 
    

## Architecture

VIDUP is divided into several independent services connected through REST APIs and message brokers. These include:

    vidup-streaming: Handles the video streaming service.
    vidup-nsfw: A Python FastAPI service responsible for NSFW content detection.
    vidup-media-procc: Responsible for media processing tasks such as transcoding and conversion.
    vidup-core: Provides the core functionality including user management, video metadata, and serves the frontend HTML.

## Diagram

  //TODO
  
## Installation

    Clone the repository:

    bash

git clone https://github.com/sayan0927/vidup

Navigate to the project directory:

Set up environment variables in .env file:


```
MONGODB_USER=root
MONGODB_PASSWORD=pass
MONGODB_DATABASE=vidup
MONGODB_DOCKER_PORT=27017
```

Start the application using Docker Compose:

bash

    docker-compose up



## Usage

After starting the application, the following services will be accessible:

    Mongo Express: http://localhost:28081
    PHPMyAdmin: http://localhost:8090
    VIDUP Core  http://localhost:8000

   Visit VIDUP Core URL and login with credentials admin,admin.

## API Endpoints
  //TODO

Here’s a breakdown of the Docker Compose setup:

  ```

networks:
  vidup-net:

services:
  mongodb:
    image: mongo
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: pass
      MONGODB_INITDB_DATABASE: vidup
    ports:
      - "27017:27017"
    networks:
      - vidup-net
    volumes:
      - mongo-data:/data/db

  mongo-express:
    image: mongo-express
    container_name: mongo-express
    restart: always
    ports:
      - "28081:8002"
    networks:
      - vidup-net
    depends_on:
      - mongodb

  mysqldb:
    image: mysql:latest
    volumes:
      - db-data:/var/lib/mysql
    restart: always
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: pass
      MYSQL_DATABASE: vidup
    networks:
      - vidup-net

  rabbitmq:
    image: rabbitmq:3.9-management-alpine
    container_name: rabbit
    ports:
        - "5672:5672"
        - "15672:15672"
    networks:
      - vidup-net

  media-procc:
    build: ./vidup-media-processing
    ports:
      - "8001:8001"
    networks:
      - vidup-net
    depends_on:
      - rabbitmq

  stream:
    build: ./vidup-streaming
    ports:
      - "8080:8080"
    networks:
      - vidup-net
    depends_on:
      - mysqldb

  core:
    build: ./vidup-core-module
    ports:
      - "8000:8000"
    networks:
      - vidup-net
    depends_on:
      - mysqldb
      - mongodb
      - rabbitmq

volumes:
  db-data:
  mongo-data:
  videostore:
    external: true

```




