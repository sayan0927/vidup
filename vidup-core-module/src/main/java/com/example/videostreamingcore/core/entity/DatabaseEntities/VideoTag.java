package com.example.videostreamingcore.core.entity.DatabaseEntities;

import com.example.videostreamingcore.core.entity.composite_ids.VideoTagPrimaryKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "video_tag")
@Data
public class VideoTag {

    @EmbeddedId
    private VideoTagPrimaryKey videoTagPrimaryKey;

}