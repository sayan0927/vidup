package org.example.vidupstreaming.storage.internal;


import org.example.vidupstreaming.storage.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service("LocalStorageServiceImpl")
class LocalStorageServiceImpl implements LocalStorageService {

    Executor defaultExecutor = Executors.newVirtualThreadPerTaskExecutor();


    @Qualifier("webApplicationContext")



    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url, long offset, long count) {
        return readFileAsBytes(url, defaultExecutor, offset, count);
    }


    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url, Executor executor, long offset, long count) {

        CompletableFuture<byte[]> future = CompletableFuture.completedFuture(url).thenApplyAsync(url1 -> {


            long length = count - offset + 1;
            //    System.out.println(path+" path normal");
            byte[] result = new byte[(int) length];

            Path path;
            try {
                path = Paths.get(url1.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return new byte[0];
            }


            try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(path))) {
                // Skip to the start position
                inputStream.skip(offset);

                // Read the specified range into the result array
                int bytesRead = inputStream.read(result, 0, (int) length);

                if (bytesRead < length) {
                    throw new IOException("Could not read the entire range from the file");
                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }

        }, executor);


        return future;
    }


    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url) {
        return readFileAsBytes(url, defaultExecutor);
    }

    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url, Executor executor) {

        CompletableFuture<byte[]> future = CompletableFuture.completedFuture(url).thenApplyAsync(u -> {

            try {
                Path localPath = getLocationFromURL(u);
                return Files.readAllBytes(localPath);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }

        }, executor);

        return future;
    }


    @Override
    public CompletableFuture<byte[]> readFileAsBytes(Path filePath) {
        return readFileAsBytes(filePath, defaultExecutor);
    }


    @Override
    public CompletableFuture<byte[]> readFileAsBytes(Path filePath, Executor executor) {

        CompletableFuture<byte[]> future = CompletableFuture.completedFuture(filePath).thenApplyAsync((path) -> {

            try {
                byte[] bytes = Files.readAllBytes(path);
                return bytes;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor).exceptionally((ex) -> {
            return new byte[0];
        });

        return future;
    }

    @Override
    public long getFileSizeInBytes(URL url) {
        try {
            Path path = Paths.get(url.toURI());
            return path.toFile().length();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }


    @Override
    public Path getLocationFromURL(URL url) throws Exception {
        return Paths.get(url.toURI());
    }

    @Override
    public long getFileSizeInBytes(Path filePath) {
        return filePath.toFile().length();
    }

}
