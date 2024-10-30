package org.example.vidupmediaprocessing.upload_pipeline.pipeline_messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@NoArgsConstructor
public class PipelineMessageHandleDash<T> extends AbstractPipelineMessage<T> {

    private String storeName;

    Path inputPath;
    Path outputPath;



    public PipelineMessageHandleDash(T videoId, String storeName,  Path inputPath, Path outputPath) {
        super(videoId);

        this.storeName = storeName;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

}
