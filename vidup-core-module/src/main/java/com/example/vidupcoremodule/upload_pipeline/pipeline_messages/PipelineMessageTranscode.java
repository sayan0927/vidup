package com.example.vidupcoremodule.upload_pipeline.pipeline_messages;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

/**
 * Message sent to Topic - "upload_transcode"
 */

@Data
@NoArgsConstructor
public class PipelineMessageTranscode<T> extends AbstractPipelineMessage<T> {
    private String storeName;
    private Path inputPath;
    private Path outputPath;

    public PipelineMessageTranscode(T videoId, String storeName, Path inputPath, Path outputPath) {
        super(videoId);

        this.storeName = storeName;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }




}
