package com.example.vidupcoremodule.storage;



import com.example.vidupcoremodule.storage.internal.AzureFileLocation;
import com.example.vidupcoremodule.storage.internal.AzureLocation;

import java.net.URL;
import java.nio.file.Path;

public interface AzureStorageService extends CloudStorageService<AzureLocation> {


    URL getFileURL(AzureFileLocation fileLocation) throws Exception;

    boolean upload(Path filePath, String fileName, AzureLocation location) throws Exception;


}
