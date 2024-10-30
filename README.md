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

