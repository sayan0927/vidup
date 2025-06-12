package com.example.vidupcoremodule.core.entity_dtos.video_dtos;

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


    @Override
    public String getFileName() {
        return mp4FileName;
    }

    @Override
    public URL getFileLocation() {
        return mp4Location;
    }
}
