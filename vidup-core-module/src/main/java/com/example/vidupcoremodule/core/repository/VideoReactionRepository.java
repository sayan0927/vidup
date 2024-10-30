package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoReaction;
import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface VideoReactionRepository extends JpaRepository<VideoReaction, VideoUserPrimaryKey> {


    List<VideoReaction> findVideoReactionByPrimaryKeyVideo(Video video);

    VideoReaction findVideoReactionByPrimaryKeyVideoAndPrimaryKeyUser(Video video, User user);

    @Query("SELECT COUNT(vr) FROM VideoReaction vr WHERE vr.primaryKey.video.id = :videoId AND vr.score = 1")
    int getLikesOnVideo(@Param("videoId") UUID videoId);

    @Query("SELECT COUNT(vr) FROM VideoReaction vr WHERE vr.primaryKey.video.id = :videoId AND vr.score = -1")
    int getDisLikesOnVideo(@Param("videoId") UUID videoId);
}
