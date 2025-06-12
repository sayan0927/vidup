package org.example.vidupstreaming.repository;

import jakarta.transaction.Transactional;
import org.example.vidupstreaming.Entity.VideoDataDashSegment;
import org.example.vidupstreaming.Entity.VideoDataMP4;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Repository
public interface VideoDataMp4Repository extends VideoDataRepository<VideoDataMP4> {



}
