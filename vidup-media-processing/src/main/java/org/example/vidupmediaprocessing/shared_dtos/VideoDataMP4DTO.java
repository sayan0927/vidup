package org.example.vidupmediaprocessing.shared_dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class VideoDataMP4DTO extends VideoDataDTO {


    String mp4FileName;

    URL mp4Location;

    String audioCodec;

    String language;


    @JsonIgnore
    @Override
    public String getFileName() {
        return mp4FileName;
    }

    @JsonIgnore
    @Override
    public URL getFileLocation() {
        return mp4Location;
    }
}
