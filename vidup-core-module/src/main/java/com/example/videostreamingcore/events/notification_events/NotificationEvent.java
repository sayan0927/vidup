package com.example.videostreamingcore.events.notification_events;

import com.example.videostreamingcore.dtos.UserDTO;
import com.example.videostreamingcore.events.AbstractEvent;
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
