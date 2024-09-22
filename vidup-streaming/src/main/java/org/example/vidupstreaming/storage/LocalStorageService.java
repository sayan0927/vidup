package org.example.vidupstreaming.storage;

import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Path;

@Service
public interface LocalStorageService extends StorageService<Path> {



    Path getLocationFromURL(URL url) throws Exception;

    long getFileSizeInBytes(Path filePath);
}
