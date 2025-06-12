package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="live_recording")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveRecording {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    User creator;

    @Column(name = "description")
    String description;

    @Column(name="recording_name")
    String name;

    @Column(name = "visibility")
    String visibility;
}
