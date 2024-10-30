package com.example.vidupcoremodule.core.util;


import com.example.vidupcoremodule.core.entity_dtos.UserMapper;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.dtos.UserDTO;
import com.example.vidupcoremodule.events.EventPublisher;
import com.example.vidupcoremodule.events.notification_events.CommentNotificationEvent;
import com.example.vidupcoremodule.events.notification_events.FailedUploadNotificationEvent;
import com.example.vidupcoremodule.events.notification_events.ReactionNotificationEvent;
import com.example.vidupcoremodule.events.notification_events.SuccessfulUploadNotificationEvent;
import com.example.vidupcoremodule.events.pipeline_events.StartPipelineEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.UUID;

@Component
public class EventUtil {


    @Autowired
    EventPublisher eventPublisher;

    @Autowired
    VideoService videoService;

    UserMapper userMapper;

    public void publishCommentNotificationEvent(UUID videoId, UserDTO generatingUser, String commentText, UserDTO targetUser)
    {
        CommentNotificationEvent event = new CommentNotificationEvent(targetUser,generatingUser,commentText,videoId);
        eventPublisher.publishEvent(event);

    }

    public void publishReactionNotificationEvent(UUID videoId, UserDTO generatingUser, UserDTO targetUser, int score)
    {
        ReactionNotificationEvent event = new ReactionNotificationEvent(targetUser,videoId,generatingUser,score);
        eventPublisher.publishEvent(event);
    }


    public void publishSuccessFullUploadNotificationEvent(UUID videoId) {

        UserDTO targetUser = userMapper.INSTANCE.userToUserDTO(videoService.getVideoById(videoId).getCreator());
        SuccessfulUploadNotificationEvent event = new SuccessfulUploadNotificationEvent(targetUser,videoId);
        eventPublisher.publishEvent(event);
    }

    public void publishFailedUploadNotificationEvent(UUID videoId, String errorMessage) {

        UserDTO targetUser = userMapper.INSTANCE.userToUserDTO(videoService.getVideoById(videoId).getCreator());
        FailedUploadNotificationEvent event = new FailedUploadNotificationEvent(targetUser,videoId,errorMessage);
        eventPublisher.publishEvent(event);
    }

    public void publishStartPipelineEvent(UUID videoId, String fileStoreName, Path fileStorePath)
    {
        StartPipelineEvent<UUID,String,Path> event = new StartPipelineEvent<>(videoId,fileStoreName,fileStorePath);
        eventPublisher.publishGenericEvent(event);
    }
}
