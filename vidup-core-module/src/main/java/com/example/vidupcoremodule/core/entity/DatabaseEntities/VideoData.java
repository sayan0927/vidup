package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.*;
import lombok.Data;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class VideoData {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;

    @Column(name = "size")
    long size;

    @Transient
    public String uniqueFileName()
    {
        return video.getId()+"_"+getFileName();
    }

    @Transient
    public abstract String getFileName();

    @Transient
    public abstract void setLocation(Object location);

    @Transient
    public abstract URL getLocation();




}
