package com.example.vidupcoremodule.notification;


import com.example.vidupcoremodule.core.util.UtilClass;
import com.example.vidupcoremodule.dtos.UserDTO;
import com.example.vidupcoremodule.events.notification_events.NotificationEvent;
import com.example.vidupcoremodule.notification.internal.handlers.NotificationHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NotificationListener {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UtilClass util;

    @Autowired
    List<? extends NotificationHandler> notificationHandlers;

    Map<Class<?>, NotificationHandler> handlerMap;

    @PostConstruct
    void init() {
        handlerMap = new HashMap<>();

        for (NotificationHandler handler : notificationHandlers)
            handler.registerHandler(handlerMap);
    }

    @ApplicationModuleListener
    public void listenForNotification(NotificationEvent event) {

        System.out.println("Received notification: on thread" + event + "  " + Thread.currentThread().getName());
        handlerMap.get(event.getClass()).handle(event);
    }


    @MessageMapping({"/notifications/pending/my"})
    public void requestPendingNotifications(Authentication authentication) {

        if (!util.isLoggedIn(authentication)) return;

        UserDTO notificationRequester = util.getUserDtoFromAuthentication(authentication);
        notificationService.handlePendingNotificationOfUser(notificationRequester);
    }

    @MessageMapping("/notifications/set_seen/{notificationId}")
    public void setNotificationAsSeen(@DestinationVariable("notificationId") String notificationId, Authentication authentication) {

        if (!util.isLoggedIn(authentication)) return;

        UserDTO notificationRequester = util.getUserDtoFromAuthentication(authentication);
        notificationService.handleNotificationSeen(notificationRequester, notificationId);
    }


}

