package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.transcoding;



import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface TranscodeService  {


    /**
     * For any uploaded video file,
     * ensure video is in compatible codecs and format
     * if not, handle transcoding to compatible formats
     *
     * @param inFileName name of file
     * @param inFilePath location of file
     * @param outputPath location where transcoded file will be place
     * @return  name of newly created file
     * @throws Exception
     */
    CompletableFuture<String> handleTranscode(String inFileName , Path inFilePath , Path outputPath) ;

    CompletableFuture<String> handleTranscode(String inFileName , Path inFilePath , Path outputPath, Executor executor);

}
