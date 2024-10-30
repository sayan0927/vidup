package org.example.vidupstreaming.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import java.net.URL;

@Entity
@Table(name = "video_data_dash_manifest")
public class VideoDataDashManifest extends VideoData {

    @Column(name = "manifest_filename")
    String manifestFileName;

    @Column(name = "manifest_location")
    URL manifestLocation;

    public URL getManifestLocation() {
        return manifestLocation;
    }

    public String getManifestFileName() {
        return manifestFileName;
    }



    @Override
    public String toString() {
        return "VideoDataDashManifest [manifestFileName=" + manifestFileName + ", manifestLocation="+manifestLocation+"videoId=" +super.videoId+ "]";
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
