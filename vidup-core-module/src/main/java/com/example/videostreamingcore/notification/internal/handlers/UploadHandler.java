package com.example.videostreamingcore.notification.internal.handlers;

import com.example.videostreamingcore.events.notification_events.FailedUploadNotificationEvent;
import com.example.videostreamingcore.events.notification_events.NotificationEvent;
import com.example.videostreamingcore.events.notification_events.SuccessfulUploadNotificationEvent;
import com.example.videostreamingcore.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadHandler implements NotificationHandler {

    @Autowired
    NotificationService notificationService;

    @Override
    public void handle(NotificationEvent event) {

        if(event instanceof SuccessfulUploadNotificationEvent)
        {
            SuccessfulUploadNotificationEvent ev = (SuccessfulUploadNotificationEvent) event;
            notificationService.handleSuccessFullUploadNotification(ev.getVideoID(),ev.getTargetUser());
        }
        else
        {
            FailedUploadNotificationEvent ev = (FailedUploadNotificationEvent) event;
            notificationService.handleFailedUploadNotification(ev.getVideoID(),ev.getErrorMessage(),ev.getTargetUser());
        }
    }

    @Override
    public void registerHandler(Map<Class<?>, NotificationHandler> registry) {

        registry.put(SuccessfulUploadNotificationEvent.class,this);
        registry.put(FailedUploadNotificationEvent.class,this);
    }
}
