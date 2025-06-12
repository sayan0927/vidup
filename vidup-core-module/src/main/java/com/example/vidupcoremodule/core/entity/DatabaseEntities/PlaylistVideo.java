package com.example.vidupcoremodule.core.entity.DatabaseEntities;



import com.example.vidupcoremodule.core.entity.composite_ids.PlaylistVideoPrimaryKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "playlist_video")
@Data
public class PlaylistVideo {


    @EmbeddedId
    PlaylistVideoPrimaryKey primaryKey;

    @Column(name = "added_date")
    LocalDateTime addedDate;
}
