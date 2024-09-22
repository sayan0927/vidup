package com.example.videostreamingcore.core.entity.DatabaseEntities;

import com.example.videostreamingcore.core.entity.composite_ids.UserUserPrimaryKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "subscription")
@Data
public class Subscription {

    @EmbeddedId
    private UserUserPrimaryKey primaryKey;
}
