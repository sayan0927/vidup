package org.example.vidupstreaming.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StreamingApplicationConstants {

    public Path videoStorePath;

    public String VIDEO;
    public String CONTENT_TYPE;
    public String CONTENT_LENGTH;
    public  String VIDEO_CONTENT;
    public String CONTENT_RANGE;
    public  String ACCEPT_RANGES;
    public Path VIDEO_STORE_PATH;
    public String BYTES;
    public  int CHUNK_SIZE;
    public  int BYTE_RANGE;
    public  String  ipV6LoopBackAddress;
    public  String localHostAddress;
    //actual ip of the server
    public  String dynamicLocalHostAddress;
    private StreamingApplicationConstants() {
    }

    @PostConstruct
    void init()
    {
        VIDEO = "/video";
        CONTENT_TYPE = "Content-Type";
        CONTENT_LENGTH = "Content-Length";
        VIDEO_CONTENT = "video/";
        CONTENT_RANGE = "Content-Range";
        ACCEPT_RANGES = "Accept-Ranges";

        BYTES = "bytes";
        CHUNK_SIZE = 512000;
        BYTE_RANGE = 1024;
        ipV6LoopBackAddress= "0.0.0.0.0.0.0.1";
        localHostAddress = "127.0.0.1";
        //actual ip of the server
        dynamicLocalHostAddress = "192.168.1.3";

        Path moduleRoot = Paths.get("").toAbsolutePath();
        Path projectRoot = moduleRoot.getParent();


        VIDEO_STORE_PATH = Paths.get("/commonstore/video");

    }
}
