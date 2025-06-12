package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_comment")
@Data
public class VideoComment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "commenter_id")
    User commenter;

    @ManyToOne
    @JoinColumn(name = "video_id")
    Video parentVideo;


    @Column(name = "text")
    String commentText;


    @Column(name = "comment_datetime")
    LocalDateTime commentDateTime;




}
