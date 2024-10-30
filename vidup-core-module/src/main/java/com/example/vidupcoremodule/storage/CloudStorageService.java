package com.example.vidupcoremodule.storage;



import com.example.vidupcoremodule.storage.internal.CloudLocation;

import java.net.URL;

public interface CloudStorageService<T extends CloudLocation> extends StorageService<T> {

    URL getFileURL(T fileLocation) throws Exception;
}
