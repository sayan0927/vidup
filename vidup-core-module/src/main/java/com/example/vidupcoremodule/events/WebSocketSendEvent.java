package com.example.vidupcoremodule.events;

import lombok.Data;

@Data
public class WebSocketSendEvent extends AbstractEvent{

    String destination;
    Object message;
}
