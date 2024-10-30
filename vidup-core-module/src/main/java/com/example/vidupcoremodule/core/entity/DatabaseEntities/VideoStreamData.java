package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vid_vstream_data")
@Data
public class VideoStreamData {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "video_id")
    Video video;

    @Column(name = "vcodec")
    String videoCodec;

    @Column(name = "duration_seconds")
    Long duration;

}
