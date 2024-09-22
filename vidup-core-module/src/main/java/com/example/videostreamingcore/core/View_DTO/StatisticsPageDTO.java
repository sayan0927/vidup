package com.example.videostreamingcore.core.View_DTO;

import com.example.videostreamingcore.core.entity.DatabaseEntities.User;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;

import java.util.List;



public record StatisticsPageDTO(int subscriberCount,int uploadCount,User creator,List<VideoStat> videoStatsList){
    public record VideoStat(Video video,int views,int viewsLastWeek,int downloads,int likes,int dislikes,int comments) {}
}



