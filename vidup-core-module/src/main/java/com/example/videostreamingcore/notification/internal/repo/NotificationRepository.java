package com.example.videostreamingcore.notification.internal.repo;

import com.example.videostreamingcore.dtos.UserDTO;
import com.example.videostreamingcore.notification.internal.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String> {



    List<Notification> findByTargetUserAndSeen(UserDTO user, Boolean seen);
}