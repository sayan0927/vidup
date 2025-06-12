package org.example.vidupmediaprocessing.upload_pipeline.pipeline_messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipelineMessageFailure<T> extends AbstractPipelineMessage<T> {

    public String failureMessage;

    public PipelineMessageFailure(T videoId,String failureMessage)
    {
        super(videoId);
        this.failureMessage=failureMessage;
    }
}
