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

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
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

    @Autowired
    CacheManager cacheManager;



    @Transactional
    public VideoDataDashSegment getDashSegmentOfVideo(UUID videoId, String dashSegmentName) {

        String key = videoId.toString() + "_" + dashSegmentName;
        System.out.println(cacheManager.getCacheNames());
        if(cacheManager.getCache(key).get(key) != null) {
            return cacheManager.getCache(key).get(videoId, VideoDataDashSegment.class);
        }

        System.out.println("FETCHING FROM DB");
        VideoDataDashSegment segment = dashSegmentRepository.findByVideoIdAndSegmentFileName(videoId, dashSegmentName);
        cacheManager.getCache(key).put(key, segment);
        return segment;
    }

    public URL resolveDashSegmentLocationFromBaseLocation(URL baseLocation,String dashSegmentName) throws MalformedURLException {
        String base = baseLocation.toString();

        return new URL(base+dashSegmentName);
    }

    public URL getBaseLocation(UUID videoId, String dashSegmentName)
    {

        if(cacheManager.getCache(videoId.toString())!=null &&
        cacheManager.getCache(videoId.toString()).get(videoId) != null) {
           // System.out.println("fetching from cache");
            return cacheManager.getCache(videoId.toString()).get(videoId, URL.class);
        }

      //  System.out.println("fetching from DB");

        VideoDataDashSegment dataDashSegment = dashSegmentRepository.findFirstByVideoId(videoId);
        String baseLocation = dataDashSegment.getLocation().toString();
        int lastSlashIdx = baseLocation.lastIndexOf('/');
        baseLocation = baseLocation.substring(0, lastSlashIdx + 1);


        System.out.println("loc is "+baseLocation);
        try {
            cacheManager.getCache(videoId.toString()).put(videoId, new URL(baseLocation));
            return new URL(baseLocation);
        }
        catch (Exception e)
        {
            return null;
        }


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
