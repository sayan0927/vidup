package com.example.videostreamingcore.notification.internal.controller;

import com.example.videostreamingcore.dtos.UserDTO;
import com.example.videostreamingcore.dtos.NotificationDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NotificationSender {

    void sendNotification(UserDTO targetUser, NotificationDTO notificationDTO);
    void sendNotification(UserDTO targetUser, List<NotificationDTO> notificationDTOs);
}
