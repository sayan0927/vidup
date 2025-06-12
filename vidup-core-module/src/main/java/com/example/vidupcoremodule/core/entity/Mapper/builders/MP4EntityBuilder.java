package com.example.vidupcoremodule.core.entity.Mapper.builders;

import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoData;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoDataMP4;
import com.example.vidupcoremodule.core.entity_dtos.video_dtos.VideoDataDTO;
import com.example.vidupcoremodule.core.entity_dtos.video_dtos.VideoDataMP4DTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MP4EntityBuilder implements VideoDataEntityBuilder{
    @Override
    public VideoData build(VideoDataDTO videoDataDTO, Video owningVideo) {

        VideoDataMP4 mp4 = new VideoDataMP4();

        VideoDataMP4DTO mp4DTO = (VideoDataMP4DTO) videoDataDTO;

        mp4.setMp4FileName(mp4DTO.getMp4FileName());
        mp4.setMp4Location(mp4DTO.getMp4Location());
        mp4.setLanguage(mp4DTO.getLanguage());
        mp4.setAudioCodec(mp4DTO.getAudioCodec());
        mp4.setVideo(owningVideo);
        mp4.setSize(videoDataDTO.getSize());

        return mp4;
    }

    @Override
    public void register(Map<Class<?>, VideoDataEntityBuilder> registry) {
        registry.put(VideoDataMP4DTO.class, this);
    }




}
