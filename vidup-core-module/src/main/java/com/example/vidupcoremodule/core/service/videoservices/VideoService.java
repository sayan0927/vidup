package com.example.vidupcoremodule.core.service.videoservices;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.*;
import com.example.vidupcoremodule.core.entity.Mapper.DtoEntityMapper;
import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import com.example.vidupcoremodule.core.entity_dtos.video_dtos.VideoDataDTO;
import com.example.vidupcoremodule.core.repository.*;
import com.example.vidupcoremodule.core.service.videoservices.searching.SearchService;
import com.example.vidupcoremodule.core.util.UtilClass;
import com.example.vidupcoremodule.storage.AzureStorageService;
import com.example.vidupcoremodule.storage.CloudStorageService;
import com.example.vidupcoremodule.storage.LocalStorageService;
import com.example.vidupcoremodule.storage.StorageUtil;
import com.example.vidupcoremodule.storage.dto.UploadFileDTO;
import com.example.vidupcoremodule.storage.internal.AzureConfig;
import com.example.vidupcoremodule.storage.internal.AzureFileLocation;
import com.example.vidupcoremodule.storage.internal.CloudLocation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class VideoService {

    @Autowired
    StorageUtil storageUtil;

    @Autowired
    DtoEntityMapper dtoEntityMapper;

    @Autowired
    UtilClass utilClass;

    @Autowired
    TagService tagService;
    String storeNameSeparator = "-";
    @Autowired
    SearchService searchService;
    @Autowired
    LocalStorageService storageService;
    @Autowired
    VideoDownloadRepository videoDownloadRepository;
    @Autowired
    AzureConfig azureConfig;
    @Autowired
    AzureStorageService azureStorageService;
    @Autowired
    VideoDataRepository<VideoDataMP4> mp4Repository;

    @Autowired
    VideoDataDashManifestRepository manifestRepository;
    @Autowired
    VideoDataRepository<VideoData> genericVideoDataRepository;
    @Autowired
    VideoDataDashSegmentRepository dashSegmentRepository;
    @Autowired
    private VideoRepository videoRepository;

    /**
     * Returns the VideoDataMP4's for a video
     *
     * @param video The video
     * @return List of VideoDataMP4 , with size>=1 , size>1 implies multiple languages
     */
    public List<VideoDataMP4> getMp4Data(Video video) {

      //  List<VideoDataMP4> mp4s = mp4Repo.findByVideo(video);



     //   System.out.println(mp4s);

            return mp4Repository.findByVideo(video);



    }


    @Transactional
    public CompletableFuture<VideoDataDashSegment> getDashSegmentOfVideoFuture(UUID videoId, String dashSegmentName) {
        Video video = getVideoById(videoId);
        return dashSegmentRepository.findBySegmentFileNameAndVideo(dashSegmentName, video);
    }

    public String getVideoName(UUID videoId) {
        if (videoRepository.existsById(videoId)) return videoRepository.findVideoById(videoId).getVideoName();

        

        return null;
    }


    public Video getVideoById(UUID videoId) {
        return videoRepository.findVideoById(videoId);
    }

    public List<Video> getAllByIds(List<UUID> videoIds) {
        return videoRepository.findAllById(videoIds);
    }

    public User getVideoCreator(UUID videoId) {

        if (!videoRepository.existsById(videoId)) return null;

        Video video = videoRepository.findVideoById(videoId);
        return video.getCreator();
    }

    public VideoDataDashManifest getManifest(UUID videoId) {

        return manifestRepository.findVideoDataDashManifestByVideoId(videoId);
    }

    public List<Video> joinWithRemaning(List<Video> videos) {
        List<Video> all = getAllVideos();
        List<Video> finalList = new ArrayList<>(videos);

        for (Video v : all) {
            if (!finalList.contains(v)) finalList.add(v);
        }
        return finalList;
    }

    public int countVideosUploadedByUser(User user) {

        System.out.println("here+" + user);
        int count = videoRepository.countByCreator(user);
        System.out.println(count);
        return count;
    }


    public Boolean deleteVideo(Video video) {

        if (!videoRepository.existsById(video.getId())) return false;

        //deleting from search index data structure
        searchService.removeFromSearchIndex(video);


        //deleting video files
        List<VideoDataMP4> files = getMp4Data(video);
        for (VideoDataMP4 stream : files) {
            String fileName = stream.getMp4FileName();
            storageService.deleteFileByFileName(fileName, storageUtil.getLocal().getFinalStorePath().resolve(fileName));
        }

        //deleting from database
        videoRepository.delete(video);
        return true;
    }

    public Boolean deleteVideo(UUID videoId) {

        if (!videoRepository.existsById(videoId)) return false;

        return deleteVideo(videoRepository.findVideoById(videoId));
    }

    public String generateStoreName(Video video, String originalFileName) {
        return video.getId() + storeNameSeparator + video.getCreator().getId() + storeNameSeparator + originalFileName;
    }

    public Video setVideoReady(Video video) {
        video.setReady(true);
        return videoRepository.save(video);
    }

    public void setVideoReady(UUID videoId) {
        Video video = videoRepository.findVideoById(videoId);
        video.setReady(true);
        videoRepository.save(video);
    }

    @Transactional
    public boolean persistDataInCloud(UUID videoId) throws Exception {
        List<VideoData> list = getAllVideoData(videoId);

        List<UploadFileDTO> uploadFileDTOList = list.stream().map(videoData -> {

            return new UploadFileDTO(videoData.getLocation(),videoData.uniqueFileName());
        }).toList();



       List<URL> newUrls = storageUtil.moveVideoFilesToCloud(uploadFileDTOList);



        for (int i = 0; i < newUrls.size(); i++) {
            list.get(i).setLocation(newUrls.get(i));
        }

        genericVideoDataRepository.saveAll(list);

        return true;


    }

    @Transactional
    public void cleanupNotReadyVideos() {
        videoRepository.deleteAllByReady(Boolean.FALSE);
    }

    public Boolean videoExists(UUID videoId) {
        return videoRepository.existsById(videoId);
    }

    public Resource downloadVideoFileAsResource(UUID videoId, User downloader) {
        Video video = videoRepository.findVideoById(videoId);


        VideoUserPrimaryKey pk = new VideoUserPrimaryKey();
        pk.setVideo(video);
        pk.setUser(downloader);

        if (!videoDownloadRepository.existsById(pk)) {
            VideoDownload vd = new VideoDownload();
            vd.setPrimaryKey(pk);
            videoDownloadRepository.save(vd);
        }

        List<VideoDataMP4> audioStreamDataList = mp4Repository.findByVideo(video);
        String fileName = utilClass.determineFileNameForDefaultLanguage(audioStreamDataList);


        Resource resource = storageService.downloadFileAsResource(fileName, storageUtil.getLocal().getFinalStorePath().resolve(fileName).normalize());

        return resource;
    }


    @Transactional
    public List<? extends VideoData> saveVideoDataDTOS(List<? extends VideoDataDTO> dtos) {

        List<VideoData> entities = new ArrayList<>();

        for (VideoDataDTO dto : dtos) {
            Video dtoOwner = getVideoById(dto.getVideoID());
            entities.add(dtoEntityMapper.buildEntity(dto, dtoOwner));
        }


        return genericVideoDataRepository.saveAll(entities);

    }

    public VideoData saveVideoData(VideoData videoData) {

        return genericVideoDataRepository.save(videoData);
    }

    public List<VideoData> getAllVideoData(UUID videoId) {
        return genericVideoDataRepository.findAllByVideo(getVideoById(videoId));
    }

    @Transactional
    public Video createVideoEntityAndSave(String originalFileName, User creator, String description, String visibility, String videoName, List<String> selectedTags) {
        Video video = new Video();

        Set<Tag> tags = tagService.getTagsById(selectedTags);

        video.setTags(tags);
        video.setCreator(creator);
        video.setVideoName(videoName);
        video.setUploadTime(LocalDateTime.now());
        video.setDescription(description);
        video.setVisibility(visibility.toLowerCase());
        video.setOrigFilename(originalFileName);


        //saving to generate id
        video = videoRepository.save(video);
        return videoRepository.save(video);

    }


    public List<Video> getAllVideos() {
        return videoRepository.findAllByReady(Boolean.TRUE);
    }

    public List<Video> getUploadVideosOfUser(User user) {

        return videoRepository.findAllByCreatorAndReady(user, Boolean.TRUE);
    }


}
