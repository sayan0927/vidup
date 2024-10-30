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

  TODO
  
## Installation

Clone the repository:

Navigate to the project directory:

Set up environment variables ( create env.env file on linux or .env file on windows )


```
EXT_SPRING_DATA_MONGODB_HOST='mongodb'
EXT_MONGODB_DATABASE='vidup'
EXT_MONGODB_DOCKER_PORT='27017'
EXT_FINAL_STORE_LOCATION='local'
EXT_SPRING_DATASOURCE_URL='jdbc:mysql://mysqldb:3306/vidup'
```
Put values of these environment variables as per your choice
```
EXT_SPRING_DATA_MONGODB_USERNAME='your_mongodb_uname'
EXT_SPRING_DATA_MONGODB_PASSWORD='your_mongodb_pass'
EXT_SPRING_MAIL_USERNAME='your_email'
EXT_SPRING_MAIL_PASSWORD='your_email_pass'
EXT_SPRING_DATASOURCE_USERNAME='your_mysql_uname'
EXT_SPRING_DATASOURCE_PASSWORD='your_mysql_pass'
EXT_SPRING_RABBITMQ_USERNAME='your_rabbit_uname'
EXT_SPRING_RABBITMQ_PASSWORD='your_rabbit_pass'
```


To use Azure Files as Storage instead of LocalStorage , set these environement variables 
 ```
    EXT_FINAL_STORE_LOCATION='azure'
    EXT_AZURE_STORAGE_ACCOUNT='your_azure_storage_account'
    EXT_AZURE_STORAGE_ACCOUNT_SAS_TOKEN="your_azure_storage_account_sas_token"
    EXT_AZURE_VIDEO_FILES_CONTAINER='your_video_files_container'
 ```


### To let the application be accessible from external network

In docker-compose-**.yml file
Set the following variables of core service and give your own static ip.

```
      STREAMING_IP: 
      CORE_IP: 

      and set 

      SPRING_PROFILES_ACTIVE: prod
```
Modify following variables of media-procc service and give your own static ip

```

"register-data.url": "http://localhost:8000/videos/processing/videoId/register_data",
"dash.base.url.template": "<BaseURL>http://localhost:8080/videos/permitted/stream/dash/VIDEO_ID/</BaseURL>",

```




### Start the application using Docker Compose:

```
    docker compose -f docker-compose-linux.yml up
```
or
```
    docker compose -f docker-compose-windows.yml up
```

## Usage

After starting the application, the following services will be accessible:

    Mongo Express: http://localhost:28081
    PHPMyAdmin: http://localhost:8090
    VIDUP Core  http://localhost:8000

   Visit VIDUP Core URL and login with credentials admin,admin.

## API Endpoints
 TODO

