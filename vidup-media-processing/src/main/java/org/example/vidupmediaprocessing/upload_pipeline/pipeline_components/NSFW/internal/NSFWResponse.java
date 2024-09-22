package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW.internal;

import lombok.Data;

@Data

/**
 * Response From 3rd party API for Single Image
 */
class NSFWResponse {

        private Double safe;
        private Double unsafe;
}
