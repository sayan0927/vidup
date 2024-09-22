package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW;


import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Exposed API
 */
public interface NSFWDetectService {


    /**
     * Checks if  uploaded video file contains NSFW content
     * @param inFileName    name of the file
     * @param inFilePath    location of the file
     * @return  True if no NSFW content,
     *          False otherwise
     */
    CompletableFuture<Boolean> videoPassedNsfwTests(String inFileName, Path inFilePath);

    CompletableFuture<Boolean> videoPassedNsfwTests(String inFileName, Path inFilePath, Executor executor);
}
