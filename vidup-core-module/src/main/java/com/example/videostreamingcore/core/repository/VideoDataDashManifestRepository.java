package com.example.videostreamingcore.core.repository;

import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoDataDashManifest;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoDataDashManifestRepository extends VideoDataRepository<VideoDataDashManifest> {

    VideoDataDashManifest findVideoDataDashManifestByVideoId(UUID videoId);
}

