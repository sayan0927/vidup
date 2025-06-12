package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.dashservice.internal;


import org.example.vidupmediaprocessing.FFMPEG.FFMpegDashHandling;
import org.example.vidupmediaprocessing.shared_dtos.VideoDataDTO;
import org.example.vidupmediaprocessing.shared_dtos.VideoDataDashManifestDTO;
import org.example.vidupmediaprocessing.shared_dtos.VideoDataDashSegmentDTO;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.dashservice.DashConfig;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.dashservice.MpegDashHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
class FFMPEGDashHandlerService implements MpegDashHandlerService {


    @Autowired
    FFMpegDashHandling dashHandler;


    @Autowired
    @Qualifier("getDefaultPipelineExecutor")
    Executor getDefaultExecutor ;

    @Autowired
    private DashConfig dashConfig;



    @Override
    public CompletableFuture<List<VideoDataDTO>> createDashFiles(String inputFileName, Path inputFileLocation, Path outputLocation, UUID videoId) {
        return createDashFiles(inputFileName, inputFileLocation, outputLocation, videoId, getDefaultExecutor);
    }

    @Override
    public CompletableFuture<List<VideoDataDTO>> createDashFiles(String inputFileName, Path inputFileLocation, Path outputLocation, UUID videoId, Executor executor) {
        String manifestName = dashConfig.manifestName;


        String baseUrl = dashConfig.baseUrlTemplate.replace(dashConfig.videoIdPlaceHolder, videoId.toString());

        CompletableFuture<List<VideoDataDTO>> future = CompletableFuture.runAsync(() -> {

            try {
                dashHandler.createMpegDash(inputFileName, inputFileLocation, outputLocation, manifestName, dashConfig.dashCommandTemplate());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor).thenApplyAsync((status) -> {


            List<VideoDataDTO> createdDTOS = new ArrayList<>();
            File[] directoryFiles = new File(outputLocation.toAbsolutePath().toString()).listFiles();

            for (File file : directoryFiles) {
                String fileName = file.getName();

                try {
                    if (fileName.endsWith(".mpd")) {

                        VideoDataDashManifestDTO manifest = new VideoDataDashManifestDTO();

                        manifest.setManifestLocation(file.toURI().toURL());
                        manifest.setManifestFileName(file.getName());
                        manifest.setVideoID(videoId);
                        manifest.setSize(file.length());
                        addBaseURL(baseUrl, manifest.getManifestLocation());

                        createdDTOS.add(manifest);
                    }

                    if (fileName.endsWith(".m4s")) {

                        VideoDataDashSegmentDTO dashSegmentDTO = new VideoDataDashSegmentDTO();
                        dashSegmentDTO.setVideoID(videoId);
                        dashSegmentDTO.setSegmentLocation(file.toURI().toURL());
                        dashSegmentDTO.setSegmentFileName(file.getName());
                        dashSegmentDTO.setSize(file.length());
                        createdDTOS.add(dashSegmentDTO);
                    }



                } catch (Exception e) {
                    return null;
                }
            }


            return createdDTOS;
        }, executor);


        return future;

    }

    void addBaseURL(String baseURL, URL manifestPath) throws Exception {

        File originalManifest = Paths.get(manifestPath.toURI()).toFile();
        String originalManifestName = originalManifest.getName();

        Path manifestBasePath = Paths.get(originalManifest.getParentFile().toURI());

        File tempManifest = Files.createFile(manifestBasePath.resolve("temp_manifest.mpd")).toFile();


        File finalManifest = manifestBasePath.resolve(originalManifestName).toFile();

        // resources will auto close because of try-with-resource
        try (FileReader reader = new FileReader(originalManifest); FileWriter writer = new FileWriter(tempManifest); BufferedReader bufferedReader = new BufferedReader(reader); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //adding baseurl before final closing tag
                if (line.equals("</MPD>")) {
                    bufferedWriter.write(baseURL);
                    bufferedWriter.newLine();
                }

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }

        Files.delete(originalManifest.toPath());
        tempManifest.renameTo(finalManifest);


    }
}
