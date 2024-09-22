package com.example.videostreamingcore.core.repository;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Reported;
import com.example.videostreamingcore.core.entity.DatabaseEntities.User;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface ReportedRepository extends JpaRepository<Reported,Integer> {

    Reported findReportedByReportedByAndVideoAndStatus(User reportedBy, Video video,String status);

    List<Reported> findAllByStatusOrderByVideo(String status);

    List<Reported> findAllByVideo(Video video);


    @Transactional
    @Modifying
    @Query("update Reported r set r.status=:status WHERE r.video=:video")
    void updateByVideo(Video video,String status);


}
