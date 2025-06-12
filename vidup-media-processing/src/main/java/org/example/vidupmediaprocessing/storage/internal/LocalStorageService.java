package org.example.vidupmediaprocessing.storage.internal;


import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.example.vidupmediaprocessing.storage.StorageProperties;
import org.example.vidupmediaprocessing.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service("LocalStorageService")
class LocalStorageService implements StorageService {


    @Autowired
    StorageProperties storageProperties;

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;


    public List<String> readLinesFromFile(Path file) throws IOException
    {
        List<String> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.toFile()));
            String l;
            while ((l=reader.readLine())!=null)
            {
                list.add(l);
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    public Path createFolder(Path absoluteBasePath, String folderName) {
        Path newFolder = absoluteBasePath.resolve(folderName);

        if (newFolder.toFile().mkdir()) {
            return newFolder;
        } else {
            return null;
        }

    }

    public boolean deleteDirectory(Path absoluteDirectoryPath) {
        File file = new File(absoluteDirectoryPath.toString());

        try {
            System.out.println(absoluteDirectoryPath);
            FileUtils.deleteDirectory(file);
            System.out.println("directory deleted");
            return true;
        } catch (IOException Exception) {
            System.out.println(Exception);
            System.out.println(absoluteDirectoryPath + " could not be deleted");
            return false;
        }
    }



    public byte[] readFileAsBytesFromClassPath(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/" + fileName);


        return resource.getContentAsByteArray();

    }


    /**
     * Appends chunks to end of a file
     * @param chunk        the chunks as byte array
     * @param filename      name of the file
     * @param filePath      path of the file
     * @return appended chunk Id
     * @throws IOException
     */
    @Override
    public void appendChunks(byte[] chunk,String filename,Path filePath) throws IOException{

        Path finalFilePath=filePath.resolve(filename);


        if(Files.exists(finalFilePath)){
            Files.write(finalFilePath,chunk,StandardOpenOption.APPEND);
        }
        else
        {
            Files.write(finalFilePath,chunk,StandardOpenOption.CREATE);
        }


    }



    @Override
    public boolean uploadFile(MultipartFile file,  String fileName,Path filePath) {
        try
        {
            if (file.isEmpty())
                return false;

            Path targetLocation =  filePath.resolve(fileName);

            System.out.println(targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public Resource downloadFileAsResource(String fileName) {
        try
        {
            Path filePath = storageProperties.getLocal().getFinalStorePath().resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists())
                return resource;
            else
                return null;
        }
        catch (MalformedURLException ex)
        {
            return null;
        }
    }

    @Override
    public boolean deleteFileByFileName(String fileName) {

        Path filePath = storageProperties.getLocal().getFinalStorePath().resolve(fileName);
        File file = new File(filePath.toUri());
        return file.delete();
    }

    @Override
    public CompletableFuture<byte[]> readFileAsBytes(Path filePath, Executor executor) {

        CompletableFuture<byte[]> future = CompletableFuture.completedFuture(filePath)
                .thenApplyAsync((path)->{

                    try {
                        byte[] bytes = Files.readAllBytes(path);
                        return bytes;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                },executor)
                .exceptionally((ex)->{
                    return null;
                });

        return future;
    }

    @Override
    public byte[] readFileAsBytes(Path filePath) {
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            return bytes;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


}
