package com.example.vidupcoremodule.core.View_DTO;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoComment;

import java.util.List;

public record DashVideoPageDTO(Video video, List<VideoComment> videoComments,
                               int likes, int dislikes, int commentsCount, int viewCount) {

}