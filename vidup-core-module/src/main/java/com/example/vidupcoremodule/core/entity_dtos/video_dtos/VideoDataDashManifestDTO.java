package com.example.vidupcoremodule.core.entity_dtos.video_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDataDashManifestDTO extends VideoDataDTO {


    String manifestFileName;
    URL manifestLocation;

    @Override
    public String getFileName() {
        return manifestFileName;
    }

    @Override
    public URL getFileLocation() {
        return manifestLocation;
    }
}
