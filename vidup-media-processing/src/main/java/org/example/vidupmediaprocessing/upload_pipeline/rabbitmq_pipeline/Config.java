package org.example.vidupmediaprocessing.upload_pipeline.rabbitmq_pipeline;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Configuration
public class Config {


    @Bean
    @Qualifier("getDefaultPipelineExecutor")
    public Executor getDefaultPipelineExecutor() {
        return new CompletableFuture<>().defaultExecutor();
    }
}
