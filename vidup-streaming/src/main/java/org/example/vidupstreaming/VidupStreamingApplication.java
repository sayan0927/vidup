package org.example.vidupstreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = "org.example.vidupstreaming.repository")
public class VidupStreamingApplication {



    public static void main(String[] args) {
        SpringApplication.run(VidupStreamingApplication.class, args);
    }

}
