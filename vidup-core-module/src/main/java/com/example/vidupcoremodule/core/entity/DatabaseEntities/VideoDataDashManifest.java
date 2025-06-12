package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.net.URI;
import java.net.URL;

@Entity
@Table(name = "video_data_dash_manifest")
@Data
public class VideoDataDashManifest extends VideoData{

    @Column(name = "manifest_filename")
    String manifestFileName;

    @Column(name = "manifest_location")
    URL manifestLocation;

    @Override
    public String toString() {
        return "VideoDataDashManifest [manifestFileName=" + manifestFileName + ", manifestLocation="+manifestLocation+"video=" +super.video+ "]";
    }



    @Override
    public String getFileName() {
        return this.manifestFileName;
    }

    @Override
    public void setLocation(Object location) {
        this.manifestLocation = (URL) location;
    }

    @Override
    public URL getLocation() {
        return manifestLocation;
    }
}
