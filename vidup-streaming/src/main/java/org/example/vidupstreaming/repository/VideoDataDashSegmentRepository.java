package org.example.vidupstreaming.repository;


import jakarta.transaction.Transactional;
import org.example.vidupstreaming.Entity.VideoDataDashSegment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;




@Repository
public interface VideoDataDashSegmentRepository extends VideoDataRepository<VideoDataDashSegment> {


    VideoDataDashSegment findByVideoIdAndSegmentFileName(UUID videoId, String segmentFileName);

    @Transactional
    @Async
    CompletableFuture<VideoDataDashSegment> findBySegmentFileNameAndVideoId(String segmentFileName, UUID videoId);
}
