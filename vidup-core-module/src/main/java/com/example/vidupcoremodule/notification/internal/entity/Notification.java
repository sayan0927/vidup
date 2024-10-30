package com.example.vidupcoremodule.notification.internal.entity;



import com.example.vidupcoremodule.dtos.UserDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
public class Notification {

    @Id
    String id;



    UserDTO targetUser;

    String message;

    String type;

    Boolean seen;

    Map<String,Object> optionalMessage;

    Map<String,Object> optionalHidden;
}
