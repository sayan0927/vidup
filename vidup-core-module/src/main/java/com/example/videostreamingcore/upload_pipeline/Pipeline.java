package com.example.videostreamingcore.upload_pipeline;

import com.example.videostreamingcore.upload_pipeline.pipeline_messages.PipelineMessageFailure;

import java.util.UUID;

public interface Pipeline<S,U,V> {


   void startPipeline(S videoId, U fileStoreName, V fileStorePath);

    void processFail(PipelineMessageFailure<UUID> message) ;
}

