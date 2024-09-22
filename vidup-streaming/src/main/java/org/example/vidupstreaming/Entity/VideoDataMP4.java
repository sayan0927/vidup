package org.example.vidupstreaming.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import java.net.URL;

@Entity
@Table(name = "video_data_mp4")
public class VideoDataMP4 extends VideoData {


    @Column(name = "mp4_filename")
    String mp4FileName;

    @Column(name = "mp4_location")
    URL mp4Location;

    @Column(name = "acodec")
    String audioCodec;

    @Column(name = "language")
    String language;

    public URL getMp4Location() {
        return mp4Location;
    }

    public String getMp4FileName() {
        return mp4FileName;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public String getLanguage() {
        return language;
    }



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
