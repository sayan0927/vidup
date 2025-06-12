package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "video_download")
@Data
public class VideoDownload {

    @EmbeddedId
    private VideoUserPrimaryKey primaryKey;
}
