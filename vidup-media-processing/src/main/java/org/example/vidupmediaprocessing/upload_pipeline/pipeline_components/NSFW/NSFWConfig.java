package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.NSFW;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

import java.nio.file.Path;
import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "nsfw")
@Data
public class NSFWConfig {

    private String fps;
    private String skipCheck;
    private String skipSymbol;
    private String outputFormat;
    private String nsfwTestsPath;
    private Path   nsfwTestPathAsPath;
    private String minimumSafePercent;
    private String baseUrl = "http://localhost:8001";

    @PostConstruct
    void init() {

        //nsfwTestPathAsPath = Paths.get(nsfwTestsPath);
        System.out.println(nsfwTestsPath);
    }

    public boolean toSkipNSFWCheck()
    {
        System.out.println("\n\n\nTo skip"+skipCheck+" "+(!skipCheck.isEmpty()));
        return skipCheck.equals(skipSymbol);
    }

    @Bean
    public WebClient webClient() {


        ConnectionProvider connectionProvider = ConnectionProvider.builder("custom")
                .maxConnections(1000)
                .pendingAcquireMaxCount(5000)
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .build();

        LoopResources loopResources = LoopResources.create("custom", 8, true);

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .runOn(loopResources)
                .responseTimeout(Duration.ofSeconds(10));

        return WebClient.builder()
                .baseUrl(baseUrl)
              //  .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }


}
