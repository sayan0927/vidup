package com.example.vidupcoremodule.events.notification_events;


import com.example.vidupcoremodule.dtos.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor

public class CommentNotificationEvent extends NotificationEvent {

    UserDTO generatingUser;
    String commentText;
    UUID videoId;

    public CommentNotificationEvent(UserDTO targetUser, UserDTO generatingUser, String commentText, UUID videoId) {
        super(targetUser);
        this.generatingUser = generatingUser;
        this.commentText = commentText;
        this.videoId = videoId;
    }


}
