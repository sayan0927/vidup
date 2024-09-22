package com.example.videostreamingcore.events.notification_events;

import com.example.videostreamingcore.dtos.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ReactionNotificationEvent extends NotificationEvent{
    UUID videoID;
    UserDTO generatingUser;
    int score;

    public ReactionNotificationEvent(UserDTO targetUser, UUID videoID, UserDTO generatingUser, int score) {
        super(targetUser);
        this.videoID = videoID;
        this.generatingUser = generatingUser;
        this.score = score;
    }


}
