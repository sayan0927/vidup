package com.example.videostreamingcore.core.repository;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoDataDashSegment;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

import static com.example.videostreamingcore.core.repository.RepositoryAsyncConfig.TASK_EXECUTOR_REPOSITORY;


@Repository
public interface VideoDataDashSegmentRepository extends VideoDataRepository<VideoDataDashSegment> {




    @Transactional
    @Async(TASK_EXECUTOR_REPOSITORY)
    CompletableFuture<VideoDataDashSegment> findBySegmentFileNameAndVideo(String segmentFileName, Video video);
}
