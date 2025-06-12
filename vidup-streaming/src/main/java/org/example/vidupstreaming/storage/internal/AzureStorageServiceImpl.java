package org.example.vidupstreaming.storage.internal;


import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobRange;
import com.azure.storage.blob.models.DownloadRetryOptions;
import jakarta.annotation.PostConstruct;
import org.example.vidupstreaming.storage.AzureStorageService;
import org.example.vidupstreaming.storage.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@ConditionalOnProperty(name = "final.store.location", havingValue = "azure")
@Service
public class AzureStorageServiceImpl implements AzureStorageService {

    Executor defaultExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Autowired
    AzureConfig azureConfig;

    @Autowired
    LocalStorageService localStorageService;

    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url, long offset, long count) {
        return readFileAsBytes(url, defaultExecutor, offset, count);
    }

    private BlobContainerClient videoFilesContainerClient;

    @PostConstruct
    void init()
    {
        videoFilesContainerClient = new BlobContainerClientBuilder()
                .endpoint(azureConfig.storageAccount)
                .sasToken(azureConfig.storageAccountSasToken)
                .containerName(azureConfig.videoFilesContainer).buildClient();
    }


    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url, Executor executor, long offset, long count) {

        AzureFileLocation fileLocation;
        try {
            fileLocation = getLocationFromURL(url);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(new byte[0]);
        }

        BlobContainerClient blobContainerClient = videoFilesContainerClient;
        BlobClient client = blobContainerClient.getBlobClient(fileLocation.blobName);


        CompletableFuture<byte[]> future = CompletableFuture.completedFuture(client)
                .thenApplyAsync(client1 -> {

                    BlobRange range = new BlobRange(offset, count);
                    DownloadRetryOptions options = new DownloadRetryOptions().setMaxRetryRequests(5);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    client1.downloadStreamWithResponse(byteArrayOutputStream, range, options, null, false,
                            Duration.ofSeconds(30), null);

                    return byteArrayOutputStream.toByteArray();
                },executor);

        return future;


    }

    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url) {
        return readFileAsBytes(url, defaultExecutor);
    }

    @Override
    public CompletableFuture<byte[]> readFileAsBytes(URL url, Executor executor) {

        try {
            AzureFileLocation blobLocation = azureConfig.getLocationFromURL(url);
            System.out.println(blobLocation + " " + blobLocation.storageAccount + " " + blobLocation.containerName + " " + blobLocation.sasToken);
            return readFileAsBytes(blobLocation);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(new byte[0]);
        }

    }


    @Override
    public CompletableFuture<byte[]> readFileAsBytes(AzureLocation location) {
        return readFileAsBytes(location, defaultExecutor);
    }


    @Override
    public CompletableFuture<byte[]> readFileAsBytes(AzureLocation location, Executor executor) {
        BlobContainerClient blobContainerClient = videoFilesContainerClient;


        AzureFileLocation fileLocation = (AzureFileLocation) location;


        CompletableFuture<byte[]> future = CompletableFuture.completedFuture(fileLocation).thenApplyAsync((loc) -> {

            BlobClient client = blobContainerClient.getBlobClient(loc.getBlobName());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            client.download(outputStream);
            return outputStream.toByteArray();
        }, executor);


        return future;
    }

    @Override
    public long getFileSizeInBytes(URL url) {
        try {
            AzureFileLocation fileLocation = getLocationFromURL(url);
            BlobContainerClient blobContainerClient = videoFilesContainerClient;


            BlobClient client = blobContainerClient.getBlobClient(fileLocation.blobName);
            return client.getProperties().getBlobSize();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public AzureFileLocation getLocationFromURL(URL url) throws Exception {
        String urlString = url.toString();

        String protocol = "https://";

        urlString = urlString.replace(protocol, "");

        String[] parts = urlString.split("/");


        String storageAccountName = protocol + parts[0];
        String containerName = parts[1];
        String blobName = parts[2];

        return new AzureFileLocation(storageAccountName, azureConfig.storageAccountSasToken, containerName, blobName);

    }


}



