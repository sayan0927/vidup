package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoStreamData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoStreamDataRepository extends JpaRepository<VideoStreamData,Integer> {

    List<VideoStreamData> findVideoStreamsByVideo(Video video);

}
