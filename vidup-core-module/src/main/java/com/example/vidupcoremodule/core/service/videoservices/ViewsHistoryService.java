package com.example.vidupcoremodule.core.service.videoservices;




import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.ViewsHistory;
import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import com.example.vidupcoremodule.core.repository.ViewsHistoryRepository;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ViewsHistoryService {


    @Autowired
    private ViewsHistoryRepository viewsHistoryRepository;

    @Autowired
    private UtilClass utilClass;

    @Autowired
    private VideoService videoService;


    /**
     * @return List of Videos watched by user sorted by last seen
     */


    public int totalViewsOnAllVideosOfUser(User user) {
        return viewsHistoryRepository.totalViewsOfUser(user);
    }
    public List<Video> getVideosWatchedByUser(User watcher) {


        List<ViewsHistory> viewsHistories = viewsHistoryRepository.findViewsHistoriesByPrimaryKeyUserOrderByLastViewDesc(watcher);

        List<Video> videosWatched = new ArrayList<>();

        for (ViewsHistory v : viewsHistories) {

            UUID vid = v.getPrimaryKey().getVideo().getId();
            Video watchedVid = videoService.getVideoById(vid);
            videosWatched.add(watchedVid);
        }


        return videosWatched;
    }

    public LocalDateTime getLastSeenOfVideoByUser(Video video, User user) {
        ViewsHistory viewsHistory = viewsHistoryRepository.findViewsHistoriesByPrimaryKeyUserAndPrimaryKeyVideo(user, video);

        if (viewsHistory == null)
            return null;

        return viewsHistory.getLastView();
    }

    public int viewCountInPreviousDays(Video video, int days) {
        return viewsHistoryRepository.countByPrimaryKey_VideoAndLastViewAfter(video, LocalDateTime.now().minusDays(days));
    }

    public int viewCount(Video video) {
        return viewsHistoryRepository.countByPrimaryKey_Video(video);
    }


    /**
     * If video previously seen by user,Updates view time of Record
     * Else Generate new Record.
     *
     * @param videoId The video that has been watched by current user
     * @return Generated ViewsHistory object
     */
    public ViewsHistory handleVideoWatched(UUID videoId,User watcher) {
        /**
         * Get viewing user and viewed video
         */
        Video video = videoService.getVideoById(videoId);


        /**
         * If previously viewed by same user, update view time and return entity
         */
        ViewsHistory viewsHistory = viewsHistoryRepository.findViewsHistoriesByPrimaryKeyUserAndPrimaryKeyVideo(watcher, video);
        if (viewsHistory != null) {
            viewsHistory.setLastView(LocalDateTime.now());
            return viewsHistoryRepository.save(viewsHistory);
        }

        /**
         * If previously NOT viewed by same user, Create New Entity and save and return
         */
        VideoUserPrimaryKey videoUserPrimaryKey = new VideoUserPrimaryKey();
        videoUserPrimaryKey.setUser(watcher);
        videoUserPrimaryKey.setVideo(video);

        viewsHistory = new ViewsHistory();
        viewsHistory.setLastView(LocalDateTime.now());
        viewsHistory.setPrimaryKey(videoUserPrimaryKey);


        try {
            viewsHistory = viewsHistoryRepository.save(viewsHistory);
        } catch (Exception e) {
        }

        return viewsHistory;

    }
}
