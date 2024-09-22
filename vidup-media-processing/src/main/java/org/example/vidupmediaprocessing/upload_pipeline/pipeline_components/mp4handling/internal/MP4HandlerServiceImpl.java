package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.mp4handling.internal;


import org.example.vidupmediaprocessing.storage.StorageProperties;
import org.example.vidupmediaprocessing.shared_dtos.VideoDataMP4DTO;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.mp4handling.MP4HandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@EnableAsync
class MP4HandlerServiceImpl implements MP4HandlerService {

    @Autowired
    StorageProperties storageProperties;

    @Autowired
    Mp4ParserWrapper mp4ParserWrapper;
    @Qualifier("getDefaultPipelineExecutor")
    @Autowired
    private Executor getDefaultPipelineExecutor;


    public CompletableFuture<List<VideoDataMP4DTO>> mp4SplitOnLanguage(String inFileName, Path inFilePath, Path outPath, UUID parentVideoId)
    {
        return mp4SplitOnLanguage(inFileName,inFilePath,outPath,parentVideoId,getDefaultPipelineExecutor);

    }

    @Override
    public CompletableFuture<List<VideoDataMP4DTO>> mp4SplitOnLanguage(String inFileName, Path inFilePath, Path outPath, UUID parentVideoId, Executor executor) {
        File file = null;
        try {

            inFilePath = inFilePath.resolve(inFileName);
            file = new File(inFilePath.toUri());

        } catch (Exception e) {
            CompletableFuture<List<VideoDataMP4DTO>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }

        CompletableFuture<List<VideoDataMP4DTO>> future = CompletableFuture.completedFuture(file).
                thenApplyAsync((f)->{

                    try {
                        return mp4ParserWrapper.createVideoContentMP4(parentVideoId, f,outPath);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                },executor);

        return future;
    }


}
