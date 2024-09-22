package com.example.videostreamingcore.core.entity.composite_ids;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Tag;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;


@Embeddable
@Data
/**
 * Composite Primary key(user_id,tag_id)
 */
public class VideoTagPrimaryKey implements Serializable {


    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    Tag tag;

}