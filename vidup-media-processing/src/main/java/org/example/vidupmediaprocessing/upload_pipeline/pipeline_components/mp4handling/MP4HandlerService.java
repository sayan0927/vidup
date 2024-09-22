package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.mp4handling;



import org.example.vidupmediaprocessing.shared_dtos.VideoDataMP4DTO;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface MP4HandlerService {


    CompletableFuture<List<VideoDataMP4DTO>> mp4SplitOnLanguage(String inFileName, Path inFilePath, Path outPath, UUID parentVideoId);

    CompletableFuture<List<VideoDataMP4DTO>> mp4SplitOnLanguage(String inFileName, Path inFilePath, Path outPath, UUID parentVideoId, Executor executor);
}
