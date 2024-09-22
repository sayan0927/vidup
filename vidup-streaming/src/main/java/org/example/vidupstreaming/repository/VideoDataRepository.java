package org.example.vidupstreaming.repository;


import org.example.vidupstreaming.Entity.VideoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoDataRepository<T extends VideoData> extends JpaRepository<T, UUID> {

    List<T> findByVideoId(UUID videoId);


    List<VideoData> findAllByVideoId(UUID videoId);


}
