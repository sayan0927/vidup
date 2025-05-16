package org.example.vidupstreaming.controller;


import org.example.vidupstreaming.Entity.VideoDataDashManifest;
import org.example.vidupstreaming.Entity.VideoDataDashSegment;
import org.example.vidupstreaming.Entity.VideoDataMP4;
import org.example.vidupstreaming.Util.UtilClass;
import org.example.vidupstreaming.constants.StreamingApplicationConstants;
import org.example.vidupstreaming.repository.VideoDataMp4Repository;
import org.example.vidupstreaming.service.VideoService;
import org.example.vidupstreaming.service.VideoStreamService;
import org.example.vidupstreaming.storage.StorageProperties;
import org.example.vidupstreaming.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/videos/permitted/")
@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.GET})
public class VideoStreamController {



    Executor defaultExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Autowired
    VideoService videoService;

    @Autowired
    StorageProperties storageProperties;

    @Autowired
    StreamingApplicationConstants constants;


    @Autowired
    UtilClass utilClass;

    @GetMapping("/dash_manifest/{videoId}")
    public ResponseEntity<?> getDashManifest(@PathVariable("videoId") String videoId) {
        VideoDataDashManifest manifest = videoService.getManifest(UUID.fromString(videoId));
        URL manifestLocation = manifest.getLocation();
        try {
            String protocol = manifestLocation.getProtocol();
            byte[] bytes = storageProperties.getStorageService(protocol).readFileAsBytes(manifestLocation).get();

            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Manifest Not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/stream/dash/{videoId}/{chunkName}")
    public CompletableFuture<ResponseEntity<?>> getDashChunk(@PathVariable("videoId") String videoId, @PathVariable(value = "chunkName", required = false) String chunkName) {

        URL baseURL = videoService.getBaseLocation(UUID.fromString(videoId),chunkName);
        CompletableFuture<ResponseEntity<?>> response = new CompletableFuture<>();

        try {
            URL newUrl  = videoService.resolveDashSegmentLocationFromBaseLocation(baseURL,chunkName);
            String protocol = baseURL.getProtocol();

            System.out.println("\nprotocol is\n" + protocol+" url "+baseURL +  " loc " + newUrl);
            CompletableFuture<byte[]> byteFuture = storageProperties.getStorageService(protocol).readFileAsBytes(newUrl);

            response = byteFuture.thenApplyAsync(bytes -> {
                System.out.println("length "+bytes.length);
                return new ResponseEntity<>(bytes, HttpStatus.OK);
            });

        } catch (Exception e) {
            e.printStackTrace();
            response.complete(new ResponseEntity<>("Dash Segment Not found", HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return response;
    }








    @GetMapping("/stream/{videoId}/{token}/{userId}")
    public CompletableFuture<ResponseEntity<?>> streamVideo(@PathVariable("videoId") String videoId, @RequestHeader(value = "Range", required = false) String httpRangeList
            , @PathVariable(value = "userId", required = false) String userId, @PathVariable(value = "token", required = false) String token) {

        UUID vid = UUID.fromString(videoId);


        VideoDataMP4 defaultMp4 = videoService.getDefaultMp4(videoId);
        URL url = defaultMp4.getLocation();

        String fileType = "mp4";
        long fileSize = defaultMp4.getSize();

        long rangeStart = 0;
        long rangeEnd = constants.CHUNK_SIZE;


        StorageService<?> storageService = storageProperties.getStorageService(url.getProtocol());

        System.out.println("range is "+httpRangeList);
        if (httpRangeList == null) {
            long finalRangeEnd = rangeEnd;
            long finalRangeStart = rangeStart;


            return storageService.readFileAsBytes(url,rangeStart,rangeEnd)
                    .thenApplyAsync(bytes -> {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header(constants.CONTENT_TYPE, constants.VIDEO_CONTENT + fileType)
                        .header(constants.ACCEPT_RANGES, constants.BYTES)
                        .header(constants.CONTENT_LENGTH, String.valueOf(finalRangeEnd))
                        .header(constants.CONTENT_RANGE, constants.BYTES + " " + finalRangeStart + "-" + finalRangeEnd + "/" + fileSize)
                        .body(bytes); // Read the object and convert it as bytes
            },defaultExecutor);
        }

        String[] ranges = httpRangeList.split("-");
        rangeStart = Long.parseLong(ranges[0].substring(6));
        if (ranges.length > 1) {
            rangeEnd = Long.parseLong(ranges[1]);
        } else {
            rangeEnd = rangeStart + constants.CHUNK_SIZE;
        }

        rangeEnd = Math.min(rangeEnd, fileSize - 1);

        final String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
        HttpStatus httpStatus;
        if (rangeEnd >= fileSize) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.PARTIAL_CONTENT;
        }

        long finalRangeStart1 = rangeStart;
        long finalRangeEnd1 = rangeEnd;


        return storageService.readFileAsBytes(url,rangeStart,rangeEnd)
                .thenApplyAsync(bytes -> {
                    return ResponseEntity.status(httpStatus)
                            .header(constants.CONTENT_TYPE, constants.VIDEO_CONTENT + fileType)
                            .header(constants.ACCEPT_RANGES, constants.BYTES)
                            .header(constants.CONTENT_LENGTH, contentLength)
                            .header(constants.CONTENT_RANGE, constants.BYTES + " " + finalRangeStart1 + "-" + finalRangeEnd1 + "/" + fileSize)
                            .body(bytes);
                },defaultExecutor);

    }



}
