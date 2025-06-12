package com.example.vidupcoremodule.upload_pipeline;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component

/*
* T is type of Video Id
*/
public abstract class AbstractLocalPipeline<T> implements Pipeline<T,String,Path> {

    @Bean
    public Executor getDefaultPipelineExecutor() {
        return new CompletableFuture<>().defaultExecutor();
    }

    public abstract void startPipeline(T videoId, String fileStoreName, Path fileStorePath);



}
