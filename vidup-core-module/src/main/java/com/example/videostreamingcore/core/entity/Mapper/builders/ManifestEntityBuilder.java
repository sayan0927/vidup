package com.example.videostreamingcore.core.entity.Mapper.builders;

import com.example.videostreamingcore.core.entity_dtos.video_dtos.VideoDataDTO;
import com.example.videostreamingcore.core.entity_dtos.video_dtos.VideoDataDashManifestDTO;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoData;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoDataDashManifest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ManifestEntityBuilder implements VideoDataEntityBuilder {



    @Override
    public VideoData build(VideoDataDTO videoDataDTO, Video owningVideo) {

        VideoDataDashManifest manifest = new VideoDataDashManifest();

        manifest.setManifestLocation(videoDataDTO.getFileLocation());
        manifest.setManifestFileName(videoDataDTO.getFileName());
        manifest.setVideo(owningVideo);
        manifest.setSize(videoDataDTO.getSize());

        return manifest;

    }

    @Override
    public void register(Map<Class<?>, VideoDataEntityBuilder> registry) {
        registry.put(VideoDataDashManifestDTO.class, this);
    }


}
