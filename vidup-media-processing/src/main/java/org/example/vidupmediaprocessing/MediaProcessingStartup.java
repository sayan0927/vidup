package org.example.vidupmediaprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MediaProcessingStartup {

    public static void main(String[] args) {
        SpringApplication.run(MediaProcessingStartup.class, args);
    }

}
