package org.example.vidupmediaprocessing.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface StorageService {

    /**
     * Appends chunks to end of a file
     * If file does not exist ,create new file and append
     * @param chunk        the chunks as byte array
     * @param filename      name of the file
     * @param filePath      path of the file
     * @return appended chunk Id
     * @throws IOException
     */
   void appendChunks(byte[] chunk,String filename,Path filePath) throws IOException;

    boolean uploadFile(MultipartFile file,String fileName,Path filePath);

    Resource downloadFileAsResource(String fileName);

    boolean deleteFileByFileName(String fileName);

    CompletableFuture<byte[]> readFileAsBytes(Path filePath, Executor executor);

    byte[] readFileAsBytes(Path filePath);


    byte[] readFileAsBytesFromClassPath(String fileName) throws IOException;

     List<String> readLinesFromFile(Path file) throws IOException;

    Path createFolder(Path absoluteBasePath, String folderName);

    boolean deleteDirectory(Path absoluteDirectoryPath);
}
