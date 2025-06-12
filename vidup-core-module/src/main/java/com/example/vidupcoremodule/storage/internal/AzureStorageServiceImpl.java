package com.example.vidupcoremodule.storage.internal;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.ProgressReceiver;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.ParallelTransferOptions;

import com.example.vidupcoremodule.storage.AzureStorageService;
import com.example.vidupcoremodule.storage.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class AzureStorageServiceImpl implements AzureStorageService {

    @Autowired
    AzureConfig azureConfig;

    @Autowired
    LocalStorageService localStorageService;

    @Override
    public void appendChunks(byte[] chunk, String filename, AzureLocation location) throws IOException {

    }

    @Override
    public boolean upload(MultipartFile file, String fileName, AzureLocation location) {
        if(file.isEmpty())
            return false;

        try {
            byte[] bytes = file.getBytes();
            return upload(bytes,fileName,location);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean upload(Resource resource, String fileName, AzureLocation location) {
        return false;
    }

    @Override
    public boolean upload(byte[] byteArr, String fileName, AzureLocation location) {
        try {

            BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                    .endpoint(location.storageAccount)
                    .sasToken(location.sasToken)
                    .containerName(location.containerName)
                    .buildClient();

            BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
            InputStream inputStream = new ByteArrayResource(byteArr).getInputStream();

            blobClient.upload(inputStream);

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean upload(Path filePath, String fileName, AzureLocation location) throws Exception {
        try {

            BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                    .endpoint(location.storageAccount)
                    .sasToken(location.sasToken)
                    .containerName(location.containerName)
                    .buildClient();

            BlobClient blobClient = blobContainerClient.getBlobClient(fileName);




            long blockSize = 2 * 1024 * 1024; //2MB
            long fileSize = localStorageService.getFileSizeInBytes(filePath);


            if(fileSize<=blockSize)
            {
                blobClient.uploadFromFile(filePath.toString());
                return true;
            }



            ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions()
                    .setBlockSizeLong(blockSize).setMaxConcurrency(2);
               //     .setProgressReceiver(bytesTransferred->{System.out.println(bytesTransferred);});

            BlobHttpHeaders headers = new BlobHttpHeaders().setContentLanguage("en-US");

            blobClient.uploadFromFile(filePath.toString(), parallelTransferOptions, headers, null, AccessTier.HOT,
                    new BlobRequestConditions(), Duration.ofMinutes(30));

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }




    @Override
    public Resource downloadFileAsResource(String fileName, AzureLocation location) {
        return null;
    }

    @Override
    public boolean deleteFileByFileName(String fileName, AzureLocation location) {
        return false;
    }

    @Override
    public CompletableFuture<byte[]> readFileAsBytes(AzureLocation location, Executor executor) {
        return null;
    }

    @Override
    public byte[] readFileAsBytes(AzureLocation location) {
        BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                .endpoint(location.storageAccount)
                .sasToken(location.sasToken)
                .containerName(location.containerName)
                .buildClient();


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AzureFileLocation fileLocation = (AzureFileLocation) location;
        BlobClient client = blobContainerClient.getBlobClient(fileLocation.getBlobName());


        client.download(outputStream);
        return outputStream.toByteArray();

    }

    @Override
    public byte[] readFileAsBytes(URL blobUrl) throws Exception{

        AzureFileLocation blobLocation = azureConfig.getLocationFromURL(blobUrl);
        System.out.println(blobLocation+" "+blobLocation.storageAccount+" "+blobLocation.containerName+" "+blobLocation.sasToken);
        return readFileAsBytes(blobLocation);


    }



    @Override
    public URL getFileURL(AzureFileLocation fileLocation) throws Exception {
        BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                .endpoint(fileLocation.storageAccount)
                .sasToken(fileLocation.sasToken)
                .containerName(fileLocation.containerName)
                .buildClient();

        BlobClient blobClient = blobContainerClient.getBlobClient(fileLocation.getBlobName());
        String url = blobClient.getBlobUrl();

        return new URL(url);

    }


    @Override
    public URL getFileURL(AzureLocation fileLocation) throws Exception {

        if(!(fileLocation instanceof AzureFileLocation))
            throw new Exception("Invalid File Location");

        return getFileURL((AzureFileLocation) fileLocation);


    }
}
