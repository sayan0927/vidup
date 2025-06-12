package com.example.vidupcoremodule.events.notification_events;


import com.example.vidupcoremodule.dtos.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FailedUploadNotificationEvent extends NotificationEvent{

    UUID videoID;
    String errorMessage;

    public FailedUploadNotificationEvent(UserDTO targetUser, UUID videoID, String errorMessage) {
        super(targetUser);
        this.videoID = videoID;
        this.errorMessage=errorMessage;
    }
}
