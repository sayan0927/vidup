package com.example.vidupcoremodule.notification.internal;

import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.dtos.NotificationDTO;
import com.example.vidupcoremodule.dtos.UserDTO;
import com.example.vidupcoremodule.notification.NotificationService;
import com.example.vidupcoremodule.notification.internal.config.NotificationConfig;
import com.example.vidupcoremodule.notification.internal.controller.NotificationSender;
import com.example.vidupcoremodule.notification.internal.entity.Notification;
import com.example.vidupcoremodule.notification.internal.repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * CRUD functions for Notif Entity
 * Also provides functions for sending Real-Time notifications to logged-in Users via websocket
 */
@Service
class NotificationServiceImpl implements NotificationService {

    public static String delim = "`";

    @Autowired
    @Qualifier("WebSocketNotificationSender")
    NotificationSender notificationSender;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private VideoService videoService;


    Notification getNotificationById(String id) {

        return notificationRepository.findById(id).get();
    }

    Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * @param videoId The Video on which Comment was made
     */
    @Override
    public void handleCommentNotification(UUID videoId, UserDTO generatingUser, String commentText, UserDTO targetUser) {


        String message = " Commented on your video" + delim + videoService.getVideoById(videoId).getVideoName();

        Map<String, Object> optionalMessage = new HashMap<>();
        optionalMessage.put("videoId", videoId);
        optionalMessage.put("videoName", videoService.getVideoName(videoId));
        optionalMessage.put("commentText", commentText);
        optionalMessage.put("generatingUser",generatingUser);

        Map<String, Object> optionalHidden = new HashMap<>();
        optionalHidden.put("comment_time", LocalDateTime.now().toString());

        Notification notification = createBasicNotification(targetUser, NotificationConfig.COMMENT, message, optionalMessage, optionalHidden);
        notificationSender.sendNotification(targetUser, getDTO(notification));


    }

    private Notification createBasicNotification( UserDTO targetUser, String type, String message, Map<String, Object> optionalMessage, Map<String, Object> optionalHidden) {
        Notification notification = new Notification();


        notification.setSeen(Boolean.FALSE);
        notification.setType(type);
        notification.setTargetUser(targetUser);
        notification.setMessage(message);
        notification.setOptionalMessage(optionalMessage);
        notification.setOptionalHidden(optionalHidden);
        return notificationRepository.save(notification);
    }

    private NotificationDTO getDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setTargetUserId(notification.getTargetUser().getId().toString());


        notificationDTO.setTargetUserName(notification.getTargetUser().getUserName());


        notificationDTO.setType(notification.getType());
        notificationDTO.setNotificationId(notification.getId());
        notificationDTO.setOptionalMessage(notification.getOptionalMessage());

        return notificationDTO;
    }


    @Override
    public void handleSuccessFullUploadNotification(UUID videoId, UserDTO targetUser) {


        String message = "Your Upload was successful";


        Map<String, Object> optionalMessage = new HashMap<>();
        optionalMessage.put("success", Boolean.TRUE);
        optionalMessage.put("videoName", videoService.getVideoName(videoId));
        optionalMessage.put("videoId", videoId);


        Notification notification = createBasicNotification(targetUser, NotificationConfig.MY_UPLOAD, message, optionalMessage, optionalMessage);

        notificationSender.sendNotification(targetUser, getDTO(notification));


    }

    @Override
    public void handleFailedUploadNotification(UUID videoId, String errorMessage, UserDTO targetUser) {


        String message = "Your Upload Failed";

        Map<String, Object> optionalMessage = new HashMap<>();
        optionalMessage.put("success", Boolean.FALSE);
        optionalMessage.put("videoName", videoService.getVideoName(videoId));
        optionalMessage.put("errormessage", errorMessage);


        Notification notification = createBasicNotification( targetUser, NotificationConfig.MY_UPLOAD, message, optionalMessage, optionalMessage);

        System.out.println("Fail notif is\n" + notification);

        notificationSender.sendNotification(targetUser, getDTO(notification));
    }

    @Override
    public void handleLikeNotification(UUID videoId, UserDTO generatingUser, UserDTO targetUser) {

        System.out.println("likehandle");

        String message;
        String type;
        message = " Liked your video" + delim + videoService.getVideoById(videoId).getVideoName();
        type = NotificationConfig.LIKE;

        Map<String, Object> options = new HashMap<>();
        options.put("videoId", videoId);
        options.put("videoName", videoService.getVideoName(videoId));
        options.put("targetUser",targetUser);
        options.put("generatingUser",generatingUser);

        Notification notification = createBasicNotification( targetUser, type, message, options, options);

        notificationSender.sendNotification(targetUser, getDTO(notification));
    }

    @Override
    public void handleDislikeNotification(UUID videoId, UserDTO generatingUser, UserDTO targetUser) {


        System.out.println("gen user \n" + generatingUser);

        String message;
        String type;
        message = " Disliked your video" + delim + videoService.getVideoById(videoId).getVideoName();
        type = NotificationConfig.DISLIKE;

        Map<String, Object> options = new HashMap<>();
        options.put("videoId", videoId);
        options.put("videoName", videoService.getVideoName(videoId));
        options.put("generatingUser",generatingUser);

        Notification notification = createBasicNotification(targetUser, type, message, options, options);

        notificationSender.sendNotification(targetUser, getDTO(notification));
    }


    @Override
    public void handlePendingNotificationOfUser(UserDTO user) {
        List<Notification> notifications = getPendingNotificationsOfUser(user);

        List<NotificationDTO> dtos = notifications.stream().map(notification -> getDTO(notification)).collect(Collectors.toList());

        notificationSender.sendNotification(user, dtos);
    }

    List<Notification> getPendingNotificationsOfUser(UserDTO user) {
        return notificationRepository.findByTargetUserAndSeen(user, Boolean.FALSE);
    }


    @Override
    public void handleNotificationSeen(UserDTO user, String notificationId) {
        Notification notif = getNotificationById(notificationId);

        if (notif == null || !notif.getTargetUser().equals(user)) return;


        notif.setSeen(true);
        saveNotification(notif);
    }


}
