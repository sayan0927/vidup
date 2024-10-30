package com.example.vidupcoremodule.upload_pipeline.pipeline_messages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PipelineMessageFinalize<T> extends AbstractPipelineMessage<T>  {

    String senderQueue;



    public PipelineMessageFinalize(T videoId, String senderQueue) {
        super(videoId);

        this.senderQueue = senderQueue;
    }
}
