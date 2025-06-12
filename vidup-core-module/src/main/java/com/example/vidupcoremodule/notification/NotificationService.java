package com.example.vidupcoremodule.notification;



import com.example.vidupcoremodule.dtos.UserDTO;

import java.util.UUID;

public interface NotificationService {

    void handleCommentNotification(UUID videoId, UserDTO generatingUser, String commentText, UserDTO targetUser);


    void handleSuccessFullUploadNotification(UUID videoId, UserDTO targetUser);

    void handleFailedUploadNotification(UUID videoId, String errorMessage, UserDTO targetUser);


    void handleLikeNotification(UUID videoId, UserDTO generatingUser, UserDTO targetUser);

    void handleDislikeNotification(UUID videoId, UserDTO generatingUser, UserDTO targetUser);

    void handlePendingNotificationOfUser(UserDTO user);

    void handleNotificationSeen(UserDTO user, String notificationId);

}
