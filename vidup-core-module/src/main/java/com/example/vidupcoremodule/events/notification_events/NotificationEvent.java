package com.example.vidupcoremodule.events.notification_events;


import com.example.vidupcoremodule.dtos.UserDTO;
import com.example.vidupcoremodule.events.AbstractEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.modulith.ApplicationModule;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent extends AbstractEvent {

    UserDTO targetUser;
}
