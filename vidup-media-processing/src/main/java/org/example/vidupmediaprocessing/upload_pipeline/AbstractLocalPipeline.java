package org.example.vidupmediaprocessing.upload_pipeline;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;



/*
* T is type of Video Id
*/
@Component
public abstract class AbstractLocalPipeline<T> implements Pipeline<T,String,Path> {




    public abstract void startPipeline(T videoId, String fileStoreName, Path fileStorePath);



}
