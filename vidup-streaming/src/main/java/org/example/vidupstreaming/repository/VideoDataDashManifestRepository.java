package org.example.vidupstreaming.repository;

import org.example.vidupstreaming.Entity.VideoDataDashManifest;
import org.example.vidupstreaming.Entity.VideoDataDashSegment;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoDataDashManifestRepository extends VideoDataRepository<VideoDataDashManifest>{

    VideoDataDashManifest findVideoDataDashManifestByVideoId(UUID videoId);
}
