package com.example.vidupcoremodule.core.controller;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.ContentChunk;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.LiveRecording;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.service.userservices.UserService;
import com.example.vidupcoremodule.core.service.videoservices.LiveRecordingService;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.util.EventUtil;
import com.example.vidupcoremodule.core.util.UtilClass;
import com.example.vidupcoremodule.storage.LocalStorageService;
import com.example.vidupcoremodule.storage.StorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@RequestMapping("/live_record")
@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
public class LiveRecordingController {


    UtilClass utilClass;
    UserService userService;
    LiveRecordingService liveRecordingService;
    LocalStorageService storageService;
    StorageUtil storageProperties;
    VideoService videoService;
    EventUtil eventUtil;

    public LiveRecordingController(UtilClass utilClass, UserService userService, LiveRecordingService liveRecordingService, LocalStorageService storageService, StorageUtil storageProperties, VideoService videoService,EventUtil eventUtil) {
        this.utilClass = utilClass;
        this.userService = userService;
        this.liveRecordingService = liveRecordingService;
        this.storageService = storageService;
        this.storageProperties = storageProperties;
        this.videoService = videoService;
        this.eventUtil = eventUtil;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startRecording(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "description", required = false, defaultValue = "") String description, @RequestParam(value = "description", required = false, defaultValue = "public") String visibility, Authentication authentication) {
        User user = utilClass.getUserDetailsFromAuthentication(authentication).getUser();
        LiveRecording liveRecording = liveRecordingService.createNewRecording(name, description, visibility, user);
        System.out.println("new recording " + liveRecording);
        return new ResponseEntity<>(liveRecording, HttpStatus.OK);
    }

    /*
    @PostMapping("/upload/whole")
    public ResponseEntity<?> uploadWhole(@RequestParam("chunk") MultipartFile file,
                                    @RequestParam("recording_id") String liveRecordingId)
    {
        System.out.println(file.getOriginalFilename()+" chunk id "+" recording id "+liveRecordingId);
        System.out.println(file.getSize());
        Path folderPath = storageProperties.getLocal().getLiveRecordBasePath().resolve(liveRecordingId);
        storageService.uploadFile(file,liveRecordingId+".webm",folderPath);
        return new ResponseEntity<>(HttpStatus.OK);
    }
     */

    @PostMapping("/upload/chunk/{recordingId}")
    public ResponseEntity<?> uploadChunk(@RequestParam("chunk") MultipartFile file, @RequestParam("chunk_id") String chunkId, @PathVariable("recordingId") String liveRecordingId) {
        System.out.println(chunkId);
        try {

            Path filePath = storageProperties.getLocal().getLiveRecordBasePath().resolve(liveRecordingId);
            // storageService.appendChunks(file.getBytes(),liveRecordingId+".webm",filePath);

            ContentChunk contentChunk = liveRecordingService.registerChunk(UUID.fromString(liveRecordingId), file.getBytes(), Integer.parseInt(chunkId));
            System.out.println(contentChunk);
            return new ResponseEntity<>(chunkId, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(chunkId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/upload/complete/{recordingId}")
    public ResponseEntity<?> uploadComplete(@PathVariable("recordingId") String recordingId) {
        try {
            liveRecordingService.completeRecording(UUID.fromString(recordingId));
            liveRecordingService.buildFileFromChunks(UUID.fromString(recordingId));

            LiveRecording recording = liveRecordingService.getRecordingById(UUID.fromString(recordingId));

            String rName = recording.getName();
            String description = recording.getDescription();
            String originalFileName = recordingId + LiveRecordingService.LIVE_RECORDING_UPLOAD_FORMAT;
            String visibility = recording.getVisibility();

            Video video = videoService.createVideoEntityAndSave(originalFileName, recording.getCreator(), description, visibility, rName, List.of());

            Path storePath = storageProperties.getLocal().getLiveRecordBasePath();
            String storeName = recordingId.concat(LiveRecordingService.LIVE_RECORDING_UPLOAD_FORMAT);
            eventUtil.publishStartPipelineEvent(video.getId(), storeName, storePath);
            return new ResponseEntity<>(video, HttpStatus.CREATED);


        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>("Invalid recording id", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
