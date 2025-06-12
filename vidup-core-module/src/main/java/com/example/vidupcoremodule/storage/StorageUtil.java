package com.example.vidupcoremodule.storage;

import com.example.vidupcoremodule.storage.dto.UploadFileDTO;
import com.example.vidupcoremodule.storage.internal.*;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
@ConfigurationProperties(prefix = "storage")
@Data
public class StorageUtil {


    @Autowired
    StorageConfig storageConfig;


    private Local local;


    public boolean finalStoreLocationIsLocal()
    {

        return storageConfig.finalStoreLocation.isEmpty() || storageConfig.finalStoreLocation.contains("local");
    }

    @PostConstruct
    public void init() throws IOException {

        System.out.println(storageConfig.finalStoreLocation +"\n\n finalloc");
        local.init();
    }

    @Autowired
    LocalStorageService localStorageService;

    @Autowired
    CloudStorageService<AzureLocation> azureStorageService;

    @Autowired
    AzureConfig azureConfig;
  //  AzureStorageService azureStorageService;


    private  <T extends CloudLocation> CloudStorageService<T> getCloudStorageService(T cloudLocation) {


        if(cloudLocation instanceof AzureLocation)
        {
            return (CloudStorageService<T>) this.azureStorageService;
        }

        return null;
    }

    public StorageService<?> getStorageService(String protocol) {
        if(protocol.contains("http")) {
            return azureStorageService;
        }
        else return localStorageService;
    }

    public List<URL> moveVideoFilesToCloud(List<UploadFileDTO> videoFileDto) throws Exception{




        List<URL> blobUrls = new ArrayList<>();

        for(int i=0;i<videoFileDto.size();i++)
        {
            URL location = videoFileDto.get(i).fileLocation;
            String name = videoFileDto.get(i).fileName;

            try {

                Path path = Paths.get(location.toURI());
                System.out.println("file size is " + path.toFile().length() + " bytes");
                CloudLocation newLocation = azureConfig.getVideoFilesContainerLocation();
                CloudStorageService<CloudLocation> cloudStorageService =   getCloudStorageService(newLocation);

                cloudStorageService.upload(path, name, newLocation);
                AzureFileLocation azureFileLocation = azureConfig.getVideoFileLocation(name);
                URL newURl = azureStorageService.getFileURL(azureFileLocation);
                blobUrls.add(newURl);

            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return blobUrls;

    }



    @Data
    public static class Local {

        private Path originalStorePath;
        private Path transcodedStorePath;
        private Path finalStorePath;
        private Path nsfwTestsPath;
        private Path liveRecordBasePath;



        void init() {

            try {
                this.originalStorePath = originalStorePath.toRealPath();
                this.transcodedStorePath = transcodedStorePath.toRealPath();
                this.finalStorePath = finalStorePath.toRealPath();
                this.nsfwTestsPath = nsfwTestsPath.toRealPath();
                this.liveRecordBasePath = liveRecordBasePath.toRealPath();
            }
            catch (IOException ioException)
            {
                throw new RuntimeException("Error! Video Store was Not Found\nPlease check application.properties");
            }

        }

        public Local(String originalStorePath, String transcodedStorePath, String finalStorePath,String nsfwTestsPath, String liveRecordBasePath){


                this.originalStorePath = Paths.get(originalStorePath);
                this.transcodedStorePath = Paths.get(transcodedStorePath);
                this.finalStorePath = Paths.get(finalStorePath);
                this.nsfwTestsPath = Paths.get(nsfwTestsPath);
                this.liveRecordBasePath = Paths.get(liveRecordBasePath);

        }

    }


}
