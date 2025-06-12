package com.example.vidupcoremodule.upload_pipeline.rabbitmq_pipeline;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity_dtos.video_dtos.VideoDataDTO;
import com.example.vidupcoremodule.core.entity_dtos.video_dtos.VideoDataWrapper;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.service.videoservices.searching.SearchService;
import com.example.vidupcoremodule.core.util.EventUtil;
import com.example.vidupcoremodule.events.pipeline_events.StartPipelineEvent;
import com.example.vidupcoremodule.storage.LocalStorageService;
import com.example.vidupcoremodule.storage.StorageUtil;
import com.example.vidupcoremodule.upload_pipeline.AbstractLocalPipeline;
import com.example.vidupcoremodule.upload_pipeline.pipeline_messages.AbstractPipelineMessage;
import com.example.vidupcoremodule.upload_pipeline.pipeline_messages.PipelineMessageFailure;
import com.example.vidupcoremodule.upload_pipeline.pipeline_messages.PipelineMessageFinalize;
import com.example.vidupcoremodule.upload_pipeline.pipeline_messages.PipelineMessageTranscode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;


@Controller
@Qualifier("RabbitLocalPipeline")
@RequestMapping("/videos/processing")
public class RabbitLocalPipeline extends AbstractLocalPipeline<UUID> {


    private final String processEndMessagesFileName = "pipeline_final_received.txt";

    @Autowired
    VideoService videoService;

    @Autowired
    SearchService searchService;
    @Autowired
    StorageUtil storageProperties;


    @Autowired
    LocalStorageService storageService;


    @Autowired
    RabbitMQSender sender;

    @Autowired
    RabbitMQConfig config;
    @Autowired
    private EventUtil notificationUtil;

    @PostMapping(value = "/{videoId}/register_data", consumes = "application/json")
    public ResponseEntity<?> registerDashFiles(@RequestBody VideoDataWrapper wrapper, @PathVariable("videoId") String videoId) {
        List<VideoDataDTO> receivedDTOS = wrapper.getVideoData();

        System.out.println("type " + wrapper.getVideoData());

        videoService.saveVideoDataDTOS(receivedDTOS);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApplicationModuleListener
    public void listenForStartPipelineEvent(StartPipelineEvent<UUID, String, ? extends Path> startPipelineEvent) {
        System.out.println("starting pipeline aa  " + startPipelineEvent);
        startPipeline(startPipelineEvent.getVideoId(), startPipelineEvent.getFileStoreName(), startPipelineEvent.getFileStorePath());
    }

    @Override
    public void startPipeline(UUID videoId, String fileStoreName, Path fileStorePath) {
        PipelineMessageTranscode<UUID> message = new PipelineMessageTranscode<>(videoId, fileStoreName, fileStorePath, storageProperties.getLocal().getTranscodedStorePath());
        Path finalStorageBasePath = storageProperties.getLocal().getFinalStorePath();
        String folderName = videoId.toString();
        Path finalStoragePath = finalStorageBasePath.resolve(folderName);

        //creating folder where all final content will be stored
        try {
            Files.createDirectory(finalStoragePath);
            Files.createFile(finalStoragePath.resolve(processEndMessagesFileName));
            sender.sendMessage(message, config.pipelineExchange, config.transcodeKey);


        } catch (IOException e) {
            e.printStackTrace();
            // notifyFail(message, e.getMessage());
            PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(videoId, e.getMessage());
            sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
        }

    }


    @RabbitListener(queues = "${rabbit.finalize.queue}")
    public void finalizeProcess(PipelineMessageFinalize<UUID> receivedMessage) {
        System.out.println(receivedMessage);

        Path finalStorageBasePath = storageProperties.getLocal().getFinalStorePath();
        String folderName = receivedMessage.getVideoId().toString();
        Path finalStoragePath = finalStorageBasePath.resolve(folderName);

        //creating folder where all final content will be stored
        try {

            //registering from which queues messages received
            Path receivedMsgFile = finalStoragePath.resolve(processEndMessagesFileName);
            if (!Files.exists(receivedMsgFile)) {
                PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), "pipeline_finalize_received.txt not found");
                sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);

                return;
            }
            File file = receivedMsgFile.toFile();
            FileWriter writer = new FileWriter(file, true);
            writer.append(receivedMessage.getSenderQueue()).append("\n");
            writer.close();


            // comparing if expected msgs and received msgs is same ,
            // if not same,
            // check if mp4 fallback is optional, if so pipeline success , else fail


            List<String> receivedSet = storageService.readLinesFromFile(receivedMsgFile);


            // mp4 process (fallback) finished earlier,
            // wait for dash to complete
            if (receivedMessage.getSenderQueue().equals(config.mp4Queue) && !receivedSet.contains(config.dashQueue))
                return;


            if (receivedSet.contains(config.dashQueue) && receivedSet.contains(config.mp4Queue))
                processSuccess(receivedMessage);
            else {
                PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), "Both mp4 and dash did not succeed");
                sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
            }




        } catch (IOException e) {
            System.out.println("final error");
            e.printStackTrace();
            PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), e.getMessage());
            sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
        }


    }


    void processSuccess(AbstractPipelineMessage<UUID> receivedMessage) {
        handleSuccess(receivedMessage);

    }

    @Async
    void handleSuccess(AbstractPipelineMessage<UUID> message) {


        if (!storageProperties.finalStoreLocationIsLocal())
        {
            try {
                videoService.persistDataInCloud(message.getVideoId());
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        Video uploaded = videoService.getVideoById(message.getVideoId());
        searchService.addToSearchIndex(uploaded);
        videoService.setVideoReady(message.getVideoId());
        notificationUtil.publishSuccessFullUploadNotificationEvent(message.getVideoId());
    }

    @Override
    @RabbitListener(queues = "fail", replyContentType = "true", errorHandler = "RabbitExceptionHandler")
    public void processFail(PipelineMessageFailure<UUID> message) {
        handleFail(message);
    }

    @Async
    void handleFail(PipelineMessageFailure<UUID> message) {
        UUID videoId = message.getVideoId();

        System.out.println("fails msg " + message);
        System.out.println("failed " + message.getVideoId());

        if (videoService.videoExists(videoId)) {
            videoService.deleteVideo(videoId);
            notificationUtil.publishFailedUploadNotificationEvent(videoId, message.failureMessage);
        }


    }
}
