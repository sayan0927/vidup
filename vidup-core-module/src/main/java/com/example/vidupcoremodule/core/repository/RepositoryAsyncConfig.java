package com.example.vidupcoremodule.core.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class RepositoryAsyncConfig implements AsyncConfigurer {


    public static final String TASK_EXECUTOR_REPOSITORY = "repositoryTaskExecutor";



    @Bean(name = TASK_EXECUTOR_REPOSITORY)
    public Executor getRepositoryAsyncExecutor() {


        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(TASK_EXECUTOR_REPOSITORY);

        executor.setCorePoolSize(200);
        executor.setMaxPoolSize(500);
        executor.setQueueCapacity(500);
        return executor;
    }






}