package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.dashservice;



import org.example.vidupmediaprocessing.shared_dtos.VideoDataDTO;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface MpegDashHandlerService {




    CompletableFuture<List<VideoDataDTO>> createDashFiles(String inputFileName, Path inputFileLocation, Path outputLocation, UUID videoId);

    CompletableFuture<List<VideoDataDTO>> createDashFiles(String inputFileName, Path inputFileLocation, Path outputLocation, UUID videoId, Executor executor);
}
