package com.example.videostreamingcore.core.repository;

import com.example.videostreamingcore.core.entity.DatabaseEntities.LiveRecording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LiveRecordingRepository extends JpaRepository<LiveRecording, UUID> {
}
