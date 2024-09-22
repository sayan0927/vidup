package com.example.videostreamingcore.core.entity.Mapper.builders;

import com.example.videostreamingcore.core.entity_dtos.video_dtos.VideoDataDTO;
import com.example.videostreamingcore.core.entity_dtos.video_dtos.VideoDataDashSegmentDTO;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoData;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoDataDashSegment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SegmentEntityBuilder implements VideoDataEntityBuilder {


    @Override
    public VideoData build(VideoDataDTO videoDataDTO, Video owningVideo)  {
        VideoDataDashSegment segment = new VideoDataDashSegment();

        segment.setSegmentLocation(videoDataDTO.getFileLocation());
        segment.setSegmentFileName(videoDataDTO.getFileName());
        segment.setVideo(owningVideo);
        segment.setSize(videoDataDTO.getSize());

        return segment;
    }

    @Override
    public void register(Map<Class<?>, VideoDataEntityBuilder> registry) {
        registry.put(VideoDataDashSegmentDTO.class, this);
    }


}
