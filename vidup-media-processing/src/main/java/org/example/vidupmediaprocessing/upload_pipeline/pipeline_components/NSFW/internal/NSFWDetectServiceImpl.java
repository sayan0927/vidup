package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.internal;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.vidupmediaprocessing.FFMPEG.FFMpegWrapper;
import org.example.vidupmediaprocessing.storage.StorageProperties;
import org.example.vidupmediaprocessing.storage.StorageService;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.NSFWConfig;
import org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.NSFWDetectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@EnableAsync
class NSFWDetectServiceImpl implements NSFWDetectService {

    @Autowired
    StorageService storageService;

    @Autowired
    NSFWConfig nsfwConfig;

    @Autowired
    FFMpegWrapper ffMpegWrapper;

    @Autowired
    StorageProperties storageProperties;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebClient webClient;

    @Autowired
    @Qualifier("NSFWScoreFetcherImplSynchronous")
    private NSFWScoreFetcher nsfwScoreFetcher;

    @Qualifier("getDefaultPipelineExecutor")
    @Autowired
    private Executor getDefaultExecutor;


    @Override
    public CompletableFuture<Boolean> videoPassedNsfwTests(String inFileName, Path inFilePath) {
        return videoPassedNsfwTests(inFileName, inFilePath, getDefaultExecutor);
    }

    public CompletableFuture<Boolean> videoPassedNsfwTests(String inFileName, Path inFilePath, Executor executor)
    {
        if(nsfwConfig.toSkipNSFWCheck())
        {
            return CompletableFuture.completedFuture(true);
        }


        //creating folder with "filename_split" as foldername
        String folderName = inFileName.split("\\.")[0];
        Path newFolder = storageService.createFolder(nsfwConfig.getNsfwTestPathAsPath(), folderName);
        //folder creation failure
        if (newFolder == null) {
            System.out.println("Folder does not exist");
            return CompletableFuture.completedFuture(false);
        }
        CompletableFuture<Boolean> nsfwFuture = CompletableFuture.supplyAsync(
                ()->{
                    ffMpegWrapper.splitVideoIntoPNG(inFilePath, inFileName, newFolder, nsfwConfig.getFps(), nsfwConfig.getOutputFormat());
                    Double[] t = new Double[]{-1d, -1d};

                    NSFWResult result;
                    try {
                        result  = nsfwScoreFetcher.getSafeUnsafeScoresFromImages(newFolder);
                    }
                    catch (Exception e) {
                        //TODO - LOG
                        System.err.println("NSFWScoreFetcher.getSafeUnsafeScoresFromImages failed");
                        System.err.println(e);
                        return false;
                    }
                    System.out.println(result);
                    storageService.deleteDirectory(newFolder);
                    return result.getSafePercent() > Integer.parseInt(nsfwConfig.getMinimumSafePercent());
                }
                );

        return nsfwFuture;

    }




}
