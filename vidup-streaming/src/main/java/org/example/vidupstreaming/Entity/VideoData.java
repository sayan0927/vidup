package org.example.vidupstreaming.Entity;

import jakarta.persistence.*;

import java.net.URL;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class VideoData {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "video_id")
    UUID videoId;

    @Column(name = "size")
    long size;

    public long getSize() {
        return size;
    }

    public UUID getVideoId() {
        return videoId;
    }

    public UUID getId() {
        return id;
    }



    @Transient
    public String uniqueFileName()
    {
        return videoId+"_"+getFileName();
    }

    @Transient
    public abstract String getFileName();

    @Transient
    public abstract void setLocation(Object location);

    @Transient
    public abstract URL getLocation();




}
