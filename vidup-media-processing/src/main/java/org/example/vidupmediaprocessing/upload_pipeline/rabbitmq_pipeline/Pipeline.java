package org.example.vidupmediaprocessing.upload_pipeline.rabbitmq_pipeline;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.vidupmediaprocessing.JwtUtil;
import org.example.vidupmediaprocessing.shared_dtos.*;
import org.example.vidupmediaprocessing.storage.StorageProperties;
import org.example.vidupmediaprocessing.storage.StorageService;

import org.example.vidupmediaprocessing.upload_pipeline.ListUtil;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.NSFWDetectService;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.dashservice.MpegDashHandlerService;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.mp4handling.MP4HandlerService;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.transcoding.TranscodeService;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_messages.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class Pipeline {

    @Autowired
    ObjectMapper objectMapper;

    int chunkSize = 5;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MpegDashHandlerService mpegDashHandlerService;
    @Autowired
    MP4HandlerService mp4HandlerService;

    @Autowired
    TranscodeService transcodeService;

    @Autowired
    NSFWDetectService nsfwDetectService;

    @Autowired
    RabbitMQSender sender;


    @Autowired
    StorageProperties storageProperties;

    @Autowired
    StorageService storageService;


    @Value("${mp4.fallback}")
    String mp4FallBack;

    @Autowired
    RabbitMQConfig config;

    @Value("${register-data.url}")
    String registerDataUrl;



    @RabbitListener(queues = "${rabbit.transcode.queue}")
    public void consumeMessage(PipelineMessageTranscode<UUID> receivedMessage) {
        String filename = receivedMessage.getStoreName();
        Path inPath = receivedMessage.getInputPath();
        Path outPath = receivedMessage.getOutputPath();

        CompletableFuture<Void> transcodeFuture = transcodeService.handleTranscode(filename, inPath, outPath).thenAccept(finalFileName -> {

            try {
                if (finalFileName == null) {
                    PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), "Could not transcode the file");
                    sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
                    return;
                }
                UUID videoId = receivedMessage.getVideoId();
                Path nsfwReadPath = storageProperties.getLocal().getNsfwTestsPath();

                // sending message to next process in pipeline - nsfw detect
                PipelineMessageNSFWDetect<UUID> message = new PipelineMessageNSFWDetect<>(videoId, finalFileName, nsfwReadPath);
                sender.sendMessage(message, config.pipelineExchange, config.nsfwKey);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }


        });

        transcodeFuture.exceptionally((ex) -> {
            ex.printStackTrace();
            PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), ex.getMessage());
            sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
            return null;
        });


    }


    @RabbitListener(queues = "${rabbit.dash.queue}")
    public void consumeMessage(PipelineMessageHandleDash<UUID> receivedMessage) {

        Path finalStorageBasePath = storageProperties.getLocal().getFinalStorePath();
        String folderName = receivedMessage.getVideoId().toString();
        Path finalStoragePath = finalStorageBasePath.resolve(folderName);
        try {
            if (!Files.exists(finalStoragePath)) Files.createDirectory(finalStoragePath);
        } catch (Exception e) {
            e.printStackTrace();
            PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), e.getMessage());
            sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
            return;
        }

        String inFileName = receivedMessage.getStoreName();
        Path inFilePath = receivedMessage.getInputPath();
        Path outPath = receivedMessage.getOutputPath();
        UUID videoID = receivedMessage.getVideoId();
        CompletableFuture<Void> dashFuture = mpegDashHandlerService.createDashFilesTest(inFileName, inFilePath, outPath, videoID).thenAccept(dtoList -> {


            if (dtoList == null || dtoList.isEmpty()) {

                PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), "Could not create the Dash files");
                sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
                return;
            }






            List<List<VideoDataDTO>> chunkedDTOList = ListUtil.partitionListIntoChunks(dtoList,chunkSize);

            for(List<VideoDataDTO> chunk:chunkedDTOList)
            {
                System.out.println("sending "+chunk);
                persistDTOs(chunk,receivedMessage.getVideoId());
            }



           // videoService.saveVideoDataDTOS(dtoList);
          //  persistDashFileDetails(dtoList);

            PipelineMessageFinalize<UUID> finalizeMessage = new PipelineMessageFinalize<>(videoID, config.dashQueue);
            sender.sendMessage(finalizeMessage, config.pipelineExchange, config.finalizeKey);

        });

        dashFuture.exceptionally((ex) -> {
            ex.printStackTrace();
            System.out.println("\ndash fail\n");
            PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), ex.getMessage());
            sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
            return null;
        });

    }





    private void persistDTOs(List<? extends VideoDataDTO> dtos,UUID videoId) throws RuntimeException
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = registerDataUrl;
        url = url.replace("videoId",videoId.toString());

        // Create an instance of VideoDataDTOListWrapper
        VideoDataDTOListWrapper wrapper = new VideoDataDTOListWrapper();
        wrapper.setVideoData(dtos);

        // Create HttpHeaders and set Content-Type and cookies
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // Add cookies to headers
        headers.add("Cookie", jwtUtil.JWT + "=" + jwtUtil.createInternalAccessToken());

        // Create HttpEntity with the body (VideoDataDTOListWrapper) and headers
        HttpEntity<VideoDataDTOListWrapper> requestEntity = new HttpEntity<>(wrapper, headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if(!response.getStatusCode().is2xxSuccessful())
        {
            System.out.println("Request failed. Status code: " + response.getStatusCode());
            throw new RuntimeException("Could not save generated dash files to db");
        }

    }

    @RabbitListener(queues = "${rabbit.mp4.queue}")
    public void consumeMessage(PipelineMessageHandleMP4<UUID> receivedMessage) {
        Path finalStorageBasePath = storageProperties.getLocal().getFinalStorePath();
        String folderName = receivedMessage.getVideoId().toString();
        Path finalStoragePath = finalStorageBasePath.resolve(folderName);
        try {
            if (!Files.exists(finalStoragePath)) Files.createDirectory(finalStoragePath);
        } catch (Exception e) {
            e.printStackTrace();
            PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), e.getMessage());
            sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
            return;
        }

        String inFileName = receivedMessage.getStoreName();
        Path inFilePath = receivedMessage.getInputPath();
        Path outPath = receivedMessage.getOutputPath();
        UUID videoID = receivedMessage.getVideoId();

        CompletableFuture<Void> mp4Future = mp4HandlerService.mp4SplitOnLanguage(inFileName, inFilePath, outPath, videoID).thenAccept(dtoList -> {

            try {
                if (dtoList == null || dtoList.isEmpty()) {
                    throw new RuntimeException("Could not create MP4 files");
                }




                persistDTOs(dtoList,receivedMessage.getVideoId());




                PipelineMessageFinalize<UUID> finalizeMessage = new PipelineMessageFinalize<>(videoID, config.mp4Queue);
                sender.sendMessage(finalizeMessage, config.pipelineExchange, config.finalizeKey);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }


        });

        mp4Future.exceptionally((ex) -> {
            //TODO
            ex.printStackTrace();
            System.out.println("\nmp4 fail\n");
            return null;

        });
        System.out.println("waiting for new mp4 Handle");
    }

    @RabbitListener(queues = "${rabbit.nsfw.queue}")
    public void consumeMessage(PipelineMessageNSFWDetect<UUID> receivedMessage) {
        String inFileName = receivedMessage.getStoreName();
        Path inFilePath = receivedMessage.getInputPath();

        CompletableFuture<Void> nsfwFuture = nsfwDetectService.videoPassedNsfwTests(inFileName, inFilePath).thenAccept((passed) -> {

            try {
                if (!passed) {
                    PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), "Video has NSFW Content");
                    sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
                    return;
                }
                UUID videoId = receivedMessage.getVideoId();

                String storeName = receivedMessage.getStoreName();
                Path inPathNextStage = storageProperties.getLocal().getTranscodedStorePath();
                Path outPathNextStage = storageProperties.getLocal().getFinalStorePath().resolve(videoId.toString());
                PipelineMessageHandleMP4<UUID> message = new PipelineMessageHandleMP4<>(videoId, storeName, inPathNextStage, outPathNextStage);
                PipelineMessageHandleDash<UUID> message2 = new PipelineMessageHandleDash<>(videoId, storeName, inPathNextStage, outPathNextStage);


                sender.sendMessage(message, config.pipelineExchange, config.dashKey);

                if (mp4FallBack != null && !mp4FallBack.equals("false"))
                    sender.sendMessage(message2, config.pipelineExchange, config.mp4Key);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }


        });

        nsfwFuture.exceptionally((ex) -> {
            ex.printStackTrace();
            PipelineMessageFailure<UUID> failMsg = new PipelineMessageFailure<>(receivedMessage.getVideoId(), ex.getMessage());
            sender.sendMessage(failMsg, config.pipelineExchange, config.failKey);
            return null;
        });
        System.out.println("waiting for new NSFW Detect");

    }

}
