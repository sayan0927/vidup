package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.LiveRecording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LiveRecordingRepository extends JpaRepository<LiveRecording, UUID> {
}
