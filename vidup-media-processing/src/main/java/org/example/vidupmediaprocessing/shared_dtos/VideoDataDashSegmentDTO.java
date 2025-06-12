package org.example.vidupmediaprocessing.shared_dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Override
    public String getFileName() {
        return segmentFileName;
    }

    @JsonIgnore
    @Override
    public URL getFileLocation() {
        return segmentLocation;
    }
}
