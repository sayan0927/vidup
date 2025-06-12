package org.example.vidupmediaprocessing.shared_dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Override
    public String getFileName() {
        return manifestFileName;
    }

    @JsonIgnore
    @Override
    public URL getFileLocation() {
        return manifestLocation;
    }
}
