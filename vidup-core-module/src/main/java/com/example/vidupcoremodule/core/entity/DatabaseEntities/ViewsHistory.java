package com.example.vidupcoremodule.core.entity.DatabaseEntities;


import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "views_history")
@Data
public class ViewsHistory {

    @EmbeddedId
    private VideoUserPrimaryKey primaryKey;

    @Column(name = "last_view")
    LocalDateTime lastView;
}
