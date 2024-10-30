package org.example.vidupmediaprocessing.storage;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConfigurationProperties(prefix = "storage")
@Data
public class StorageProperties {


    private Local local;

    @PostConstruct
    public void init() throws IOException {
        local.init();
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
