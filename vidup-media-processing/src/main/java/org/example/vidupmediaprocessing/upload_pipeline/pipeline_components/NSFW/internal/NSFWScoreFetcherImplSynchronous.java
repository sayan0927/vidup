package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Path;

/**
 * Uses RestClient to fetch Data Synchronously
 */
@Service
class NSFWScoreFetcherImplSynchronous implements NSFWScoreFetcher {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Takes location of a folder, fetches safe/unsafe scores of all the images in the folder, then returns average of safe/unsafe score
     * @param folderPath The folder where all images are located
     * @return
     */
    public NSFWResult getSafeUnsafeScoresFromImages(Path folderPath) throws RestClientException, ResourceAccessException,JsonProcessingException {

        File[] splitPngFiles = folderPath.toFile().listFiles();

        int count = 0;
        Double[] avgScores = new Double[]{0d, 0d};

        for (File splitPngFile : splitPngFiles) {

            String url = "http://localhost:8001/?file_path=" + folderPath + "&file_name=" + splitPngFile.getName();


            try {
                ResponseEntity<String> response = new RestTemplate().getForEntity(url, String.class);

                NSFWResponse nsfwResponse = objectMapper.readValue(response.getBody(), NSFWResponse.class);
                avgScores[0] = avgScores[0] + nsfwResponse.getSafe();
                avgScores[1] = avgScores[1] + nsfwResponse.getUnsafe();
                count++;

            } catch (JsonProcessingException | RestClientException e) {
                //TODO - LOG
                throw e;
            }

        }
        avgScores[0] = avgScores[0] / count;
        avgScores[1] = avgScores[1] / count;

        NSFWResult result = new NSFWResult();
        result.setSafePercent(avgScores[0] * 100);
        result.setUnsafePercent(avgScores[1] * 100);
        return result;

    }
}
