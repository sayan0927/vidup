package com.example.videostreamingcore.core.repository;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {

    Tag findTagByTagName(String tagName);

    Tag findTagById(Integer id);
}
