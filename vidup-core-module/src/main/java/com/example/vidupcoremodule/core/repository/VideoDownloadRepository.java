package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoDownload;
import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDownloadRepository extends JpaRepository<VideoDownload, VideoUserPrimaryKey> {

    int countByPrimaryKey_Video(Video video);
}
