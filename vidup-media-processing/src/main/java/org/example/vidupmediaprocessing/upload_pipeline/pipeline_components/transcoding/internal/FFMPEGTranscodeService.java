package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.transcoding.internal;



import org.example.vidupmediaprocessing.FFMPEG.FFMpegWrapper;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.transcoding.TranscodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@EnableAsync
@Qualifier("FFMPEGTranscodeService")
class FFMPEGTranscodeService implements TranscodeService {




    @Autowired
    FFMpegWrapper ffmpegWrapper;


    @Qualifier("getDefaultPipelineExecutor")
    @Autowired
    private Executor getDefaultExecutor;


    private String transcodeToGivenVcodecAndAcodecWithMP4Container(String inputFileName, Path inputFilePath, Path outputfilePath, String videoCodec, String audioCodec) throws Exception {
        return ffmpegWrapper.transcodeToGivenVcodecAndAcodecWithMP4Container(inputFileName, inputFilePath, outputfilePath, videoCodec, audioCodec);
    }


    private String copyToMP4(String inputFileName, Path inputFilePath, Path outputFilePath) throws Exception {
        return ffmpegWrapper.copyToMP4(inputFileName, inputFilePath, outputFilePath);
    }


    private boolean compatibleCodecs(String fileName, Path filePath) throws Exception {
        return ffmpegWrapper.compatibleCodecs(fileName, filePath);
    }

    public CompletableFuture<String> handleTranscode(String inFileName, Path inFilePath, Path outputPath)
    {
       return handleTranscode(inFileName,inFilePath,outputPath, getDefaultExecutor);
    }

    @Override
    public CompletableFuture<String> handleTranscode(String inFileName, Path inFilePath, Path outputPath, Executor executor)
    {
        boolean compatible;
        try {
             compatible = compatibleCodecs(inFileName,inFilePath);
        }
        catch (Exception e) {
            e.printStackTrace();
            CompletableFuture<String> c = new CompletableFuture<>();
            c.completeExceptionally(e);
            return c;
        }

        return CompletableFuture.supplyAsync(()->{
            try {
                if(compatible)
                    return copyToMP4(inFileName,inFilePath,outputPath);
                else
                    return transcodeToGivenVcodecAndAcodecWithMP4Container(inFileName,inFilePath,outputPath,"libx264","aac");
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        },executor);



    }


}
