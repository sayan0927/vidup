Assumptions - All modules have access to videostore

External Dependencies - Ensure MySQL, Kafka, FFMPEG is installed in system

How to run project

1) Open Intellij Idea -> Open -> Select "video-streaming-core" folder

2) Goto Services Tab -> Ensure both "VidupStreaming" and "VidupCoreStartup" modules are present under "Spring Boot" 
			( If not present , open Project Structure of video-streaming-core -> Goto Modules Tab -> Import Module -> Select "video-streaming-module-webflux-master")


3) Open video-streaming-core/src/main/resources/application.properties
3.1) If needed, change MySQL URL(existing is 3306), Kafka Broker URL (existing is 9902)
3.2) Enter your gmail credentials

4) Open video-streaming-module-webflux-master/src/main/resources/application.properties
4.1) If needed, change MySQL URL(existing is 3306)


5) Open CoreApplicationConstansts.java in core module, modify first three paths as needed

6) Open StreamingApplicationConstants.java in streaming module, modify first path as needed.

7) Import vidup.sql in MySQL.

8) Ensure MySQL, Kafka Broker is running

9) Run both modules from Intellij Idea.

10) Go to localhost:800

