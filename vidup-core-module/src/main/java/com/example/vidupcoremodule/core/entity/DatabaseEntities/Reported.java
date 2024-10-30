package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reported")
public class Reported {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    User reportedBy;

    @Column(name = "reported_at")
    LocalDateTime reportedAt;

    @Column(name = "reason")
    String reason;

    @Column(name = "status")
    String status;
}
