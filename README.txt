# VIDUP – Video Streaming Application
Overview

VIDUP is a video streaming web application designed to handle video uploads, real-time streaming, webcam recording, downloading, and a host of user engagement features such as likes/dislikes, comments, playlists, and subscriptions. The application utilizes a hybrid microservice and modulith architecture to provide a scalable, flexible platform that can process videos, detect NSFW content, and manage content efficiently across different services.

The backend is the primary focus of this project, with a proof-of-concept frontend built using Thymeleaf. Though functional, a more robust client-side application is required for production readiness.
Key Features

    Video Uploads: Support for the most common video formats, with MP4 used for storage.
    Real-Time Streaming: Utilizes MPEG-DASH for adaptive bitrate streaming, ensuring smooth playback across varying network conditions and devices.
    Webcam Recording: Users can record videos directly from their browsers.
    Search and Filter: Powerful search functionality with tagging and filtering capabilities.
    User Engagement: Users can interact with content through comments, likes/dislikes, playlists, and subscriptions.
    NSFW Detection: Videos are automatically checked for inappropriate content using a separate Python microservice.
    Statistics and Analytics: Content creators can access performance metrics on their videos.
    Event-Driven Architecture: Message brokers are used for seamless communication between microservices.

Technologies Used

    Backend: Java Spring Boot, FastAPI (Python), MySQL, MongoDB, RabbitMQ
    Video Processing: FFmpeg for transcoding and conversion
    Frontend: Thymeleaf (Proof of Concept)
    Containerization: Docker for microservice orchestration
    Cloud Storage: Azure Files for high availability of video content
    Messaging: RabbitMQ for event-driven communication
    Streaming Protocols: MPEG-DASH for adaptive streaming

Architecture

VIDUP is divided into several independent services connected through REST APIs and message brokers. These include:

    vidup-streaming: Handles the video streaming service.
    vidup-nsfw: A Python FastAPI service responsible for NSFW content detection.
    vidup-media-procc: Responsible for media processing tasks such as transcoding and conversion.
    vidup-core: Provides the core functionality including user management, video metadata, and serves the frontend HTML.

Diagram

  //TODO

    Docker & Docker Compose
    MongoDB
    MySQL
    RabbitMQ
    FFmpeg

Installation

    Clone the repository:

    bash

git clone https://github.com/sayan0927/vidup

Navigate to the project directory:

bash

cd vidup

Set up environment variables in .env file:

bash

MONGODB_USER=root
MONGODB_PASSWORD=pass
MONGODB_DOCKER_PORT=27017
MYSQL_ROOT_PASSWORD=pass
MYSQL_DATABASE=vidup
MYSQL_PASSWORD=pass

Start the application using Docker Compose:

bash

    docker-compose up

This will pull and build the necessary images and run all the microservices in separate containers.
Services

    MongoDB: For storing metadata related to videos, users, and comments.
    MySQL: User authentication and other core data.
    RabbitMQ: Message broker for event-driven communication between services.
    FFmpeg: Media processing library used in the media processing microservice.

Usage

After starting the application, the following services will be accessible:

    MongoDB: http://localhost:27017
    Mongo Express: http://localhost:28081
    PHPMyAdmin: http://localhost:8090
    VIDUP Streaming Service: http://localhost:8080
    VIDUP Core Service: http://localhost:8000
    Media Processing Service: http://localhost:8001

API Endpoints
  //TODO

Here’s a breakdown of the Docker Compose setup:



