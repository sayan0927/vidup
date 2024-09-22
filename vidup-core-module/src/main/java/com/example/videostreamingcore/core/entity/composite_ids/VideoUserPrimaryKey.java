package com.example.videostreamingcore.core.entity.composite_ids;

import com.example.videostreamingcore.core.entity.DatabaseEntities.User;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Embeddable
@Data
/**
 * Composite Primary key(user_id,video_id)
 */
public class VideoUserPrimaryKey implements Serializable {


    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}