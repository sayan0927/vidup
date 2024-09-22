package com.example.videostreamingcore.core.entity.composite_ids;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Playlist;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class PlaylistVideoPrimaryKey implements Serializable {

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;
}
