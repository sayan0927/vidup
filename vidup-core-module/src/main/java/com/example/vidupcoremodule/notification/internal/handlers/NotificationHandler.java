package com.example.vidupcoremodule.notification.internal.handlers;



import com.example.vidupcoremodule.events.notification_events.NotificationEvent;

import java.util.Map;

public interface NotificationHandler {

     void handle(NotificationEvent event);

     void registerHandler(Map<Class<?>,NotificationHandler> registry);
}
