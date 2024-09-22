package com.example.videostreamingcore.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class NotificationDTO {

    String notificationId;
    String targetUserName,  targetUserId;
    String type;
    Map<String,Object> optionalMessage;

}
