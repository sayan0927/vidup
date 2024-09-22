package com.example.videostreamingcore.core.View_DTO;

import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoDataMP4;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoComment;

import java.util.List;


public record VideoPageDTO(Video video,List<VideoDataMP4> audioStreamDataList,
                           List<String> availableLanguages,List<VideoComment> videoComments,
                           int likes,int dislikes,int commentsCount,String defaultLang,int viewCount,Boolean multiLang) {














}
