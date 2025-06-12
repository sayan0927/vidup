package com.example.vidupcoremodule.core.entity.Mapper.builders;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoData;
import com.example.vidupcoremodule.core.entity_dtos.video_dtos.VideoDataDTO;

import java.util.Map;

public interface VideoDataEntityBuilder {


    VideoData build(VideoDataDTO videoDataDTO, Video owningVideo);

    void register(Map<Class<?> , VideoDataEntityBuilder> registry);


}
