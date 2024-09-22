package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.internal;

import java.nio.file.Path;

interface NSFWScoreFetcher {

    /**
     * Takes location of a folder, fetches safe/unsafe scores of all the images in the folder, then returns average of safe/unsafe score
     * @param folderPath The folder where all images are located
     * @return
     */
    NSFWResult getSafeUnsafeScoresFromImages(Path folderPath) throws Exception;
}
