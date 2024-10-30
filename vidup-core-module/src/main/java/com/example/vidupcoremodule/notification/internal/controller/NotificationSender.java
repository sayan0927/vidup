package com.example.vidupcoremodule.notification.internal.controller;


import com.example.vidupcoremodule.dtos.NotificationDTO;
import com.example.vidupcoremodule.dtos.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NotificationSender {

    void sendNotification(UserDTO targetUser, NotificationDTO notificationDTO);
    void sendNotification(UserDTO targetUser, List<NotificationDTO> notificationDTOs);
}
