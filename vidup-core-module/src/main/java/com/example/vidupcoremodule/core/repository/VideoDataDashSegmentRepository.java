package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoDataDashSegment;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

import static com.example.vidupcoremodule.core.repository.RepositoryAsyncConfig.TASK_EXECUTOR_REPOSITORY;


@Repository
public interface VideoDataDashSegmentRepository extends VideoDataRepository<VideoDataDashSegment> {




    @Transactional
    @Async(TASK_EXECUTOR_REPOSITORY)
    CompletableFuture<VideoDataDashSegment> findBySegmentFileNameAndVideo(String segmentFileName, Video video);
}
