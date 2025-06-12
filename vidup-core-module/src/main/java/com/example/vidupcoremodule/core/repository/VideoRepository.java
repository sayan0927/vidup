package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    Video findVideoById(UUID videoId);

    List<Video> findAllByCreatorAndReady(User creator, Boolean ready);

    List<Video> findAllByReady(Boolean ready);

    /**
     * Returns number of Videos Uploaded by creator
     * @param creator   the creator
     * @return          number of Videos Uploaded by creator
     */
    int countByCreator(User creator);

    @Transactional
    @Modifying
    void deleteAllByReady(boolean ready);

}
