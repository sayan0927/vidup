package com.example.vidupcoremodule.core.entity_dtos.video_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDataDashSegmentDTO extends VideoDataDTO{



    String segmentFileName;


    URL segmentLocation;

    @Override
    public String getFileName() {
        return segmentFileName;
    }

    @Override
    public URL getFileLocation() {
        return segmentLocation;
    }
}
