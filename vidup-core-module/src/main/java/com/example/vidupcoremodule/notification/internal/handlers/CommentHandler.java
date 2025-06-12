package com.example.vidupcoremodule.notification.internal.handlers;


import com.example.vidupcoremodule.events.notification_events.CommentNotificationEvent;
import com.example.vidupcoremodule.events.notification_events.NotificationEvent;
import com.example.vidupcoremodule.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommentHandler implements NotificationHandler {

    @Autowired
    NotificationService notificationService;

    @Override
    public void handle(NotificationEvent event)
    {
        System.out.println("here");
        CommentNotificationEvent e = (CommentNotificationEvent) event;
        notificationService.handleCommentNotification(e.getVideoId(),e.getGeneratingUser(),e.getCommentText(),e.getTargetUser());
    }

    @Override
    public void registerHandler(Map<Class<?>,NotificationHandler> registry) {

        registry.put(CommentNotificationEvent.class,this);
    }
}
