package com.example.vidupcoremodule.notification.internal.controller;


import com.example.vidupcoremodule.dtos.NotificationDTO;
import com.example.vidupcoremodule.dtos.UserDTO;
import com.example.vidupcoremodule.events.EventPublisher;
import com.example.vidupcoremodule.events.WebSocketSendEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("WebSocketNotificationSender")
class WebSocketNotificationSender implements NotificationSender {


    @Autowired
    EventPublisher eventPublisher;

    @Override
    public void sendNotification(UserDTO targetUser, NotificationDTO notificationDTO) {

        WebSocketSendEvent event = new WebSocketSendEvent();
        event.setDestination("/queue/notifications/" + targetUser.getId().toString());
        event.setMessage(notificationDTO);
        eventPublisher.publishEvent(event);
        // webSocketSender.sendMessageToDestination(event);

        //    webSocketSender.sendMessageToDestination("/queue/notifications/" + targetUser.getId().toString(),notificationDTO);
    }

    @Override
    public void sendNotification(UserDTO targetUser, List<NotificationDTO> dtoList) {

        for (NotificationDTO notificationDTO1 : dtoList) {
            sendNotification(targetUser, notificationDTO1);
        }

    }


}
