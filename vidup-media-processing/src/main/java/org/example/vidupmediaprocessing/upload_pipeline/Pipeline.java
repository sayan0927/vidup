package org.example.vidupmediaprocessing.upload_pipeline;



import org.example.vidupmediaprocessing.upload_pipeline.pipeline_messages.PipelineMessageFailure;

import java.util.UUID;

public interface Pipeline<S,U,V> {


   void startPipeline(S videoId, U fileStoreName, V fileStorePath);

    void notifyFail(PipelineMessageFailure<UUID> message) ;
}

