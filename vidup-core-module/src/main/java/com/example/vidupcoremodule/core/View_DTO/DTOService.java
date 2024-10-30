package com.example.vidupcoremodule.core.View_DTO;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.*;
import com.example.vidupcoremodule.core.repository.VideoDownloadRepository;
import com.example.vidupcoremodule.core.service.userservices.PlaylistService;
import com.example.vidupcoremodule.core.service.userservices.SubscriptionService;
import com.example.vidupcoremodule.core.service.videoservices.VideoCommentService;
import com.example.vidupcoremodule.core.service.videoservices.VideoReactionService;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.service.videoservices.ViewsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DTOService {

    @Autowired
    PlaylistService playlistService;

    @Autowired
    VideoService videoService;

    @Autowired
    ViewsHistoryService viewsHistoryService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    VideoReactionService videoReactionService;

    @Autowired
    VideoCommentService videoCommentService;



    @Autowired
    VideoDownloadRepository videoDownloadRepository;



    public List<VideoPageDTO> getContainersForVideos(List<Video> videos)
    {
        List<VideoPageDTO> videoPageDTOS = new ArrayList<>();

        for (Video currVideo:videos)
        {
            UUID videoId = currVideo.getId();
            VideoPageDTO currContainer = getVideoDataContainerFromVideoId(videoId);
            videoPageDTOS.add(currContainer);
        }

        return videoPageDTOS;
    }

    /**
     * Returns dtos for all videos videos in users channel
     * @return      List of dtos of video. dto contains(video,viewsOnVideo)
     */
    public List<VideoDTO> getVideoDTOsFromVideos()
    {
        List<Video> videos = videoService.getAllVideos();
        return getVideoDTOsFromVideos(videos);
    }

    /**
     * Returns dtos for videos in users channel
     * @param user  the user
     * @return      List of dtos of video. dto contains(video,viewsOnVideo)
     */
    public List<VideoDTO> getUsersVideosDTO(User user)
    {
        List<Video> videosOfUser = videoService.getUploadVideosOfUser(user);

        List<VideoDTO> videoDTOs = new ArrayList<>();
        for (Video video : videosOfUser)
        {
            int views = viewsHistoryService.viewCount(video);
            VideoDTO videoDTO = new VideoDTO(video,views);
            videoDTOs.add(videoDTO);
        }

        return videoDTOs;
    }

    public List<SubscriptionDTO> getUsersSubscriptionsDTO(User user) {
        List<User> usersSubscribedTo = subscriptionService.getUsersSubscribedTo(user);

        List<SubscriptionDTO> list = new ArrayList<>();

        for (User userSubscribedTo : usersSubscribedTo) {
            int uploadCount = videoService.countVideosUploadedByUser(userSubscribedTo);
            int subCount = subscriptionService.subscriberCount(userSubscribedTo);
            int totalViews = viewsHistoryService.totalViewsOnAllVideosOfUser(userSubscribedTo);
            SubscriptionDTO subscriptionDTO = new SubscriptionDTO(userSubscribedTo,uploadCount,subCount,totalViews);
            list.add(subscriptionDTO);


        }
        return list;
    }

    public List<PlaylistDTO> getUsersPlaylistsDTO(User user) {

        List<Playlist> userPlaylists = playlistService.getPlayListsOfUser(user);
        List<PlaylistDTO> records =  playlistService.getVideoCountsOfPlaylists(userPlaylists);
        return records;

    }

    public StatisticsPageDTO getStatisticsOfUser(User user)
    {

        List<Video> videosUploaded = videoService.getUploadVideosOfUser(user);
        int subscriberCount = subscriptionService.subscriberCount(user);
        int uploadCount = videosUploaded.size();

        List<StatisticsPageDTO.VideoStat> videoStatList = new ArrayList<>();

        for (Video video : videosUploaded) {

            int likes = videoReactionService.positiveReactions(video);
            int dislikes = videoReactionService.negativeReactions(video);
            int comments = videoCommentService.countCommentsOnVideo(video);
            int views = viewsHistoryService.viewCount(video);
            int viewLastWeek = viewsHistoryService.viewCountInPreviousDays(video, 7);
            int downloads = videoDownloadRepository.countByPrimaryKey_Video(video);

            StatisticsPageDTO.VideoStat videoStat = new StatisticsPageDTO.VideoStat(video,views,viewLastWeek,downloads,likes,dislikes,comments);

            videoStatList.add(videoStat);
        }

        StatisticsPageDTO statisticsPageDTO = new StatisticsPageDTO(subscriberCount,uploadCount,user,videoStatList);
        return statisticsPageDTO;
    }

    public List<VideoDTO> getVideoDTOsFromVideos(List<Video> videos) {


        List<VideoDTO> dtoList = new ArrayList<>();

        for (Video video : videos) {

            int views = viewsHistoryService.viewCount(video);
            VideoDTO videoDTO = new VideoDTO(video,views);
            dtoList.add(videoDTO);
        }


        return dtoList;
    }

    public DashVideoPageDTO getDashPageDTO(UUID videoId)
    {
        Video video = videoService.getVideoById(videoId);

        List<VideoComment> videoComments = videoCommentService.findAllByParentVideo(video);

        videoComments.sort((o1, o2) -> {
            LocalDateTime firstTime = o1.getCommentDateTime();
            LocalDateTime secondTime = o2.getCommentDateTime();
            return secondTime.compareTo(firstTime);
        });


        int likes = videoReactionService.positiveReactions(video);
        int dislikes = videoReactionService.negativeReactions(video);
        int commentsCount = videoCommentService.countCommentsOnVideo(video);
        int views = viewsHistoryService.viewCount(video);

        DashVideoPageDTO dto = new DashVideoPageDTO(video,videoComments,likes,dislikes,commentsCount,views);
        return dto;

    }

    public VideoPageDTO getVideoDataContainerFromVideoId(UUID videoId)
    {
        Video video = videoService.getVideoById(videoId);
        List<VideoDataMP4> audioStreamDataList = videoService.getMp4Data(video);

        System.out.println(audioStreamDataList);
        List<VideoComment> videoComments = videoCommentService.findAllByParentVideo(video);

        videoComments.sort((o1, o2) -> {
            LocalDateTime firstTime = o1.getCommentDateTime();
            LocalDateTime secondTime = o2.getCommentDateTime();
            return secondTime.compareTo(firstTime);
        });



        List<String> availableLanguages = new ArrayList<>();

        boolean hasEng=false;
        for(VideoDataMP4 v: audioStreamDataList)
        {
            if(v.getLanguage().equals("eng"))
            {
                hasEng=true;
                continue;
            }
            availableLanguages.add(v.getLanguage());
        }

        if(hasEng)
            availableLanguages.add(0,"eng");



        int likes = videoReactionService.positiveReactions(video);
        int dislikes = videoReactionService.negativeReactions(video);
        int commentsCount = videoCommentService.countCommentsOnVideo(video);
        int views = viewsHistoryService.viewCount(video);
        String defaultLang = "eng";
        boolean multiLang = availableLanguages.size() > 1;
        VideoPageDTO videoPageDTO = new VideoPageDTO(video,audioStreamDataList,availableLanguages,videoComments,likes,dislikes,commentsCount,defaultLang,views,multiLang);


        return videoPageDTO;
    }

    public List<PlaylistEntryDTO> getPlaylistsVideosDTO(String playlistId)
    {
        Playlist playlist = playlistService.getPlaylistById(playlistId);
        List<Video> playlistVideos = playlistService.getVideosOfPlaylistSortedByAddedDate(playlist);

        List<PlaylistEntryDTO> dtos = new ArrayList<>();

        for (Video video : playlistVideos) {
            int views = viewsHistoryService.viewCount(video);

            PlaylistEntryDTO dto = new PlaylistEntryDTO(video,video.getCreator(),video.getUploadTime(),views);
            dtos.add(dto);
        }

        return dtos;
    }
}
