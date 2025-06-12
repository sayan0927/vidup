package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import jakarta.persistence.*;
import lombok.Data;

import java.net.URL;

@Entity
@Table(name = "video_data_mp4")
@Data
public class VideoDataMP4 extends VideoData {


    @Column(name = "mp4_filename")
    String mp4FileName;

    @Column(name = "mp4_location")
    URL mp4Location;

    @Column(name = "acodec")
    String audioCodec;

    @Column(name = "language")
    String language;



    public String getStoreFileNameWithoutFormat()
    {
        return mp4FileName.split("\\.")[0];
    }



    @Override
    public String getFileName() {
        return this.mp4FileName;
    }

    @Override
    public void setLocation(Object location) {

        this.mp4Location = (URL) location;
    }

    @Override
    public URL getLocation() {
        return this.mp4Location;
    }
}
