package org.example.vidupmediaprocessing.upload_pipeline.pipeline_messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

/**
 * Message sent to Topic - "handle_mp4_upon_nsfw_pass"
 */

@Data
@NoArgsConstructor
public class PipelineMessageHandleMP4<T> extends AbstractPipelineMessage<T> {

    private String storeName;

    Path inputPath;
    Path outputPath;



    public PipelineMessageHandleMP4(T videoId, String storeName, Path inputPath, Path outputPath) {
        super(videoId);

        this.storeName = storeName;

        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }








    @Override
    public String toString()
    {
        return storeName+" "+" "+videoId;
    }
}
