package org.example.vidupmediaprocessing.shared_dtos;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_ARRAY, use = JsonTypeInfo.Id.MINIMAL_CLASS)
public abstract class VideoDataDTO {

    UUID videoID;

    long size;

    public abstract String getFileName();

    public abstract URL getFileLocation();
}
