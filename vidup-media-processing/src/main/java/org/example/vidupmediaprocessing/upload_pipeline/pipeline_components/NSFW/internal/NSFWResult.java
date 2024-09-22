package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.internal;

import lombok.Data;

@Data
/**
 * Cumulative of all received responses from 3rd party API
 */
class NSFWResult {
    // Getters and setters
    private double unsafePercent;
    private double safePercent;

}