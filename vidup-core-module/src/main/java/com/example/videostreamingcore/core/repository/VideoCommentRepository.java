package com.example.videostreamingcore.core.repository;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.VideoComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoCommentRepository  extends JpaRepository<VideoComment,Integer> {

    List<VideoComment> findAllByParentVideo(Video parentVideo);

    int countByParentVideo(Video video);
}