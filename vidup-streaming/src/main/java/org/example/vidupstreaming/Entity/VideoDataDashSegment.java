package org.example.vidupstreaming.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import java.net.URL;

@Entity
@Table(name = "video_data_dash_segment")
public class VideoDataDashSegment extends VideoData {

    @Column(name = "segment_filename")
    String segmentFileName;

    @Column(name = "segment_location")
    URL segmentLocation;

    public URL getSegmentLocation() {
        return segmentLocation;
    }

    public String getSegmentFileName() {
        return segmentFileName;
    }



    @Override
    public String toString() {
        return "VideoDataDashSegment [segmentFileName=" + segmentFileName + ", segmentLocation="+segmentLocation+"videoId=" +super.videoId+ "]";
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
