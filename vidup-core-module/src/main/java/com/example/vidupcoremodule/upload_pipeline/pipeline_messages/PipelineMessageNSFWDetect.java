package com.example.vidupcoremodule.upload_pipeline.pipeline_messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

/**
 * Message sent to Topic - "nsfw_detect"
 */
@Data
@NoArgsConstructor
public class PipelineMessageNSFWDetect<T> extends AbstractPipelineMessage<T> {



    private String storeName;
    private Path inputPath;


    public PipelineMessageNSFWDetect(T videoId, String storeName, Path inputPath) {
        super(videoId);

        this.storeName = storeName;
        this.inputPath = inputPath;
    }


}
