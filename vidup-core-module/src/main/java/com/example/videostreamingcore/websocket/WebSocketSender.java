package com.example.videostreamingcore.websocket;


import com.example.videostreamingcore.events.WebSocketSendEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSender {



    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMessageToDestination(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, message);
    }

    @ApplicationModuleListener
    public void sendMessageToDestination(WebSocketSendEvent sendEvent) {
        messagingTemplate.convertAndSend(sendEvent.getDestination(), sendEvent.getMessage());
    }
}