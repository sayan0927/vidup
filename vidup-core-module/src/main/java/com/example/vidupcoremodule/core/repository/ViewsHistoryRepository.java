package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.ViewsHistory;
import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewsHistoryRepository extends JpaRepository<ViewsHistory, VideoUserPrimaryKey> {

    ViewsHistory findViewsHistoriesByPrimaryKeyUserAndPrimaryKeyVideo(User user, Video video);

    List<ViewsHistory> findViewsHistoriesByPrimaryKeyUserOrderByLastViewDesc(User user);

    List<ViewsHistory> findViewsHistoriesByPrimaryKeyVideo(Video video);


    int countByPrimaryKey_Video(Video video);

    int countByPrimaryKey_VideoAndLastViewAfter(Video video, LocalDateTime lastView);

    @Query("SELECT count(*) FROM Video  v,ViewsHistory  vh WHERE v.creator=:creator and v.id=vh.primaryKey.video.id")
    Integer totalViewsOfUser(@Param("creator") User creator);

}
