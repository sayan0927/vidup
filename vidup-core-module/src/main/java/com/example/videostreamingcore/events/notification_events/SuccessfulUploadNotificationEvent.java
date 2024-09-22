package com.example.videostreamingcore.events.notification_events;

import com.example.videostreamingcore.dtos.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SuccessfulUploadNotificationEvent extends NotificationEvent{

    UUID videoID;

    public SuccessfulUploadNotificationEvent(UserDTO targetUser, UUID videoID) {
        super(targetUser);
        this.videoID = videoID;
    }


}
