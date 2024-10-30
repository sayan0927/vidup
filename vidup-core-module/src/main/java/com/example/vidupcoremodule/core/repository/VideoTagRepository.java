package com.example.vidupcoremodule.core.repository;

import com.example.vidupcoremodule.core.entity.DatabaseEntities.Tag;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoTag;
import com.example.vidupcoremodule.core.entity.composite_ids.VideoTagPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoTagRepository extends JpaRepository<VideoTag, VideoTagPrimaryKey> {

    List<VideoTag> findByVideoTagPrimaryKeyTag(Tag tag);
}
