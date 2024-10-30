package com.example.vidupcoremodule.notification.internal.handlers;

import com.example.vidupcoremodule.events.notification_events.CommentNotificationEvent;
import com.example.vidupcoremodule.events.notification_events.NotificationEvent;
import com.example.vidupcoremodule.events.notification_events.ReactionNotificationEvent;
import com.example.vidupcoremodule.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReactionHandler implements NotificationHandler {

    @Autowired
    NotificationService notificationService;

    @Override
    public void handle(NotificationEvent event) {
        System.out.println("here 2");
        ReactionNotificationEvent e = (ReactionNotificationEvent) event;
        System.out.println(e);

        if(e.getScore()>0)
            notificationService.handleLikeNotification(e.getVideoID(),e.getGeneratingUser(),e.getTargetUser());
        else
            notificationService.handleDislikeNotification(e.getVideoID(),e.getGeneratingUser(),e.getTargetUser());
    }

    @Override
    public void registerHandler(Map<Class<?>, NotificationHandler> registry) {

        registry.put(ReactionNotificationEvent.class,this);
    }
}
