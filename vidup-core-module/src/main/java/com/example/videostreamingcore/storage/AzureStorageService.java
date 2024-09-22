package com.example.videostreamingcore.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.example.videostreamingcore.storage.internal.AzureFileLocation;
import com.example.videostreamingcore.storage.internal.AzureLocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.Executor;

public interface AzureStorageService extends StorageService<AzureLocation>{



    URL getFileURL(AzureFileLocation fileLocation) throws Exception;

    boolean upload(Path filePath, String fileName, AzureLocation location) throws Exception;



}
