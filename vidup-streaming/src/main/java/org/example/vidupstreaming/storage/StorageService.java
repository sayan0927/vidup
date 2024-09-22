package org.example.vidupstreaming.storage;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public interface StorageService<T> {


    CompletableFuture<byte[]> readFileAsBytes(URL url, Executor executor, long offset, long count);
    CompletableFuture<byte[]> readFileAsBytes(URL url, long offset, long count);

    CompletableFuture<byte[]> readFileAsBytes(URL url,Executor executor);
    CompletableFuture<byte[]> readFileAsBytes(URL url);

    CompletableFuture<byte[]> readFileAsBytes(T location);
    CompletableFuture<byte[]> readFileAsBytes(T location,Executor executor);

    long getFileSizeInBytes(URL url);



}
