package org.example.vidupstreaming.service;


import jakarta.transaction.Transactional;

import org.example.vidupstreaming.Entity.VideoData;
import org.example.vidupstreaming.Entity.VideoDataDashManifest;
import org.example.vidupstreaming.Entity.VideoDataDashSegment;
import org.example.vidupstreaming.Entity.VideoDataMP4;
import org.example.vidupstreaming.Util.UtilClass;
import org.example.vidupstreaming.repository.VideoDataDashManifestRepository;
import org.example.vidupstreaming.repository.VideoDataDashSegmentRepository;
import org.example.vidupstreaming.repository.VideoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class VideoService {



    @Autowired
    UtilClass utilClass;



    @Autowired
    VideoDataRepository<VideoDataMP4> genericMp4Repo;
    @Autowired
    VideoDataDashManifestRepository dashManifestRepository;
    @Autowired
    VideoDataRepository<VideoData> genericVideoDataRepository;
    @Autowired
    VideoDataDashSegmentRepository dashSegmentRepository;



    @Transactional
    public VideoDataDashSegment getDashSegmentOfVideo(UUID videoId, String dashSegmentName) {
        return dashSegmentRepository.findByVideoIdAndSegmentFileName(videoId, dashSegmentName);
    }

    @Transactional
    public CompletableFuture<VideoDataDashSegment> getDashSegmentOfVideoFuture(UUID videoId, String dashSegmentName) {
        return dashSegmentRepository.findBySegmentFileNameAndVideoId(dashSegmentName, videoId);
    }


    public VideoDataDashManifest getManifest(UUID videoId) {
        return dashManifestRepository.findVideoDataDashManifestByVideoId(videoId);
    }


    public List<VideoData> getAllVideoData(UUID videoId) {
        return genericVideoDataRepository.findAllByVideoId(videoId);
    }

    public List<VideoDataMP4> getMp4Data(UUID videoId)
    {
        return genericMp4Repo.findByVideoId(videoId);
    }


}
