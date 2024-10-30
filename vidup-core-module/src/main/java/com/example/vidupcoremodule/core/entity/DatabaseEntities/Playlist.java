package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;


    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "last_modified")
    LocalDateTime lastModified;

    @Column(name = "name")
    String name;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_video",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    private Set<Video> roles = new HashSet<>();

}
