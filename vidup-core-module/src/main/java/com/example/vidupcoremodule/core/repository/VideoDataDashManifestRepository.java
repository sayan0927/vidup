package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoDataDashManifest;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoDataDashManifestRepository extends VideoDataRepository<VideoDataDashManifest> {

    VideoDataDashManifest findVideoDataDashManifestByVideoId(UUID videoId);
}

