package com.example.videostreamingcore.core.entity.Mapper.builders;

import com.example.videostreamingcore.core.entity_dtos.video_dtos.VideoDataDTO;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoData;

import java.util.Map;

public interface VideoDataEntityBuilder {


    VideoData build(VideoDataDTO videoDataDTO, Video owningVideo);

    void register(Map<Class<?> , VideoDataEntityBuilder> registry);


}
