package com.example.videostreamingcore.core.entity.Mapper;

import com.example.videostreamingcore.core.entity_dtos.video_dtos.VideoDataDTO;
import com.example.videostreamingcore.core.entity.Mapper.builders.VideoDataEntityBuilder;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DtoEntityMapper {


    Map<Class<?>, VideoDataEntityBuilder> availableBuilders = new HashMap<>();

    @Autowired
    List<? extends VideoDataEntityBuilder> videoDataBuilders;

    @PostConstruct
    public void init() {

        for(VideoDataEntityBuilder builder : videoDataBuilders) {
            builder.register(availableBuilders);
        }
    }

    public VideoData buildEntity(VideoDataDTO videoDataDTO, Video owningVideo) {

        return availableBuilders.get(videoDataDTO.getClass()).build(videoDataDTO,owningVideo);
    }
}