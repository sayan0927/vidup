package com.example.videostreamingcore.notification.internal.handlers;

import com.example.videostreamingcore.events.notification_events.NotificationEvent;

import java.util.Map;

public interface NotificationHandler {

     void handle(NotificationEvent event);

     void registerHandler(Map<Class<?>,NotificationHandler> registry);
}
