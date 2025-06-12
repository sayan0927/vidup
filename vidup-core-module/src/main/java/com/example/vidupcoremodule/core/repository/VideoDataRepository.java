package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface VideoDataRepository<T extends VideoData> extends JpaRepository<T, UUID> {

    List<T> findByVideo(Video video);
    List<VideoData> findAllByVideo(Video video);
}
