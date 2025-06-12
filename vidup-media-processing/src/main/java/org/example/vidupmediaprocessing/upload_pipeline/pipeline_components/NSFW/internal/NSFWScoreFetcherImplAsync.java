package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses WebClient to fetch Data Asynchronously
 */
@Service
class NSFWScoreFetcherImplAsync implements NSFWScoreFetcher {

    @Autowired
    WebClient webClient;

    /**
     * Takes location of a folder, fetches safe/unsafe scores of all the images in the folder, then returns average of safe/unsafe score
     * @param folderPath The folder where all images are located
     * @return
     */
    public NSFWResult getSafeUnsafeScoresFromImages(Path folderPath) {
        File[] splitPngFiles = folderPath.toFile().listFiles();
        List<File> files = new ArrayList<>(List.of(splitPngFiles));

        //Calling the 3rd Party API
        // vals will hold all the safe/unsafe scores(NSFWResponse) for all files in folderPath
        Flux<NSFWResponse> vals = Flux.fromIterable(files).flatMap(file -> webClient.get().
                uri("http://localhost:8001",
                        uri -> uri.
                                queryParam("file_path", folderPath.toString()).
                                queryParam("file_name", file.getName()).
                                build()).
                retrieve().
                bodyToMono(NSFWResponse.class));

        return vals.collectList().map(responses -> {
            Double[] avgScores = new Double[]{0d, 0d};
            int count = 0;
            for (NSFWResponse response : responses) {
                avgScores[0] = avgScores[0] + response.getSafe();
                avgScores[1] = avgScores[1] + response.getUnsafe();
                count++;
            }
            avgScores[0] = avgScores[0] / count;
            avgScores[1] = avgScores[1] / count;

            NSFWResult result = new NSFWResult();
            result.setSafePercent(avgScores[0] * 100);
            result.setUnsafePercent(avgScores[1] * 100);
            return result;
        }).block();

    }
}
