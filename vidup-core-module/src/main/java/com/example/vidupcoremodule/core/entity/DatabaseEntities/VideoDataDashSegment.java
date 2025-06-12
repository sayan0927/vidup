package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.net.URL;

@Entity
@Table(name = "video_data_dash_segment")
@Data
public class VideoDataDashSegment extends VideoData {

    @Column(name = "segment_filename")
    String segmentFileName;

    @Column(name = "segment_location")
    URL segmentLocation;

    @Override
    public String toString() {
        return "VideoDataDashSegment [segmentFileName=" + segmentFileName + ", segmentLocation="+segmentLocation+"video=" +super.video+ "]";
    }



    @Override
    public String getFileName() {
        return this.segmentFileName;
    }

    @Override
    public void setLocation(Object location) {
        this.segmentLocation = (URL) location;
    }

    @Override
    public URL getLocation() {
        return this.segmentLocation;
    }
}
