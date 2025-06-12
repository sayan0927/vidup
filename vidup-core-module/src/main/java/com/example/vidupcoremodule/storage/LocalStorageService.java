package com.example.vidupcoremodule.storage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public interface LocalStorageService extends StorageService<Path> {



    Path getLocationFromURL(URL url) throws Exception;

    byte[] readFileAsBytesFromClassPath(String fileName) throws IOException;

    List<String> readLinesFromFile(Path file) throws IOException;

    Path createFolder(Path absoluteBasePath, String folderName);

    boolean deleteDirectory(Path absoluteDirectoryPath);

    long getFileSizeInBytes(Path filePath);
}
