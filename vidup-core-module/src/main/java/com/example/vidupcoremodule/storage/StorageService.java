package com.example.vidupcoremodule.storage;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface StorageService<T> {


    void appendChunks(byte[] chunk, String filename, T location) throws IOException;

    boolean upload(MultipartFile file, String fileName, T location);

    boolean upload(Resource resource, String fileName, T location);

    boolean upload(byte[] byteArr, String fileName, T location);

    boolean upload(Path filePath, String fileName, T location) throws Exception;

    Resource downloadFileAsResource(String fileName, T location);

    boolean deleteFileByFileName(String fileName, T location);

    CompletableFuture<byte[]> readFileAsBytes(T location, Executor executor);

    byte[] readFileAsBytes(T location);

    byte[] readFileAsBytes( URL url ) throws Exception;



}
