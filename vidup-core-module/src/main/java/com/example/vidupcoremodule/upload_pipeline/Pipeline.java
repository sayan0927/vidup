package com.example.vidupcoremodule.upload_pipeline;



import com.example.vidupcoremodule.upload_pipeline.pipeline_messages.PipelineMessageFailure;

import java.util.UUID;

public interface Pipeline<S,U,V> {


   void startPipeline(S videoId, U fileStoreName, V fileStorePath);

    void processFail(PipelineMessageFailure<UUID> message) ;
}

