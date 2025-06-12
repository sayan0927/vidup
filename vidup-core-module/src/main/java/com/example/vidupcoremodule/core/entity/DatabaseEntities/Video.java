package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "video")
public class Video {

    @PrePersist
    void init() {
        ready=Boolean.FALSE;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "upload_time")
    LocalDateTime uploadTime;

    @Column(name = "description")
    String description;

    @Column(name = "visibility")
    String visibility;

    @Column(name = "ready")
    Boolean ready;

    @Column(name = "orig_filename")
    String origFilename;



    @Column(name = "video_name")
    String videoName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "video_tag",
            joinColumns = @JoinColumn(name = "video_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();






    public Video() {
    }



}
