package com.example.vidupcoremodule.core.entity.composite_ids;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
/**
 * Composite Primary key(user_id,user_id)
 */
public class UserUserPrimaryKey implements Serializable{

    @ManyToOne
    @JoinColumn(name = "subscribed_by")
    User subscriber;

    @ManyToOne
    @JoinColumn(name = "subscribed_to")
    User subscribedTo;


}

