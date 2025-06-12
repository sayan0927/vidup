package com.example.vidupcoremodule.core.View_DTO;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;

import java.util.List;



public record StatisticsPageDTO(int subscriberCount, int uploadCount, User creator, List<VideoStat> videoStatsList){
    public record VideoStat(Video video, int views, int viewsLastWeek, int downloads, int likes, int dislikes, int comments) {}
}



