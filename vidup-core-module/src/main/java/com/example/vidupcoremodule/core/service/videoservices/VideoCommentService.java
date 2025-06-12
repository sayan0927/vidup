package com.example.vidupcoremodule.core.service.videoservices;




import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoComment;
import com.example.vidupcoremodule.core.repository.VideoCommentRepository;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VideoCommentService {


    @Autowired
    UtilClass utilClass;
    @Autowired
    VideoService videoService;
    @Autowired
    private VideoCommentRepository videoCommentRepository;

    public int countCommentsOnVideo(Video video) {
        return videoCommentRepository.countByParentVideo(video);
    }


    public List<VideoComment> findAllByParentVideo(Video parentVideo) {
        return videoCommentRepository.findAllByParentVideo(parentVideo);
    }

    public void sendCommentNotification(UUID videoId, User commenter) {
        Video video = videoService.getVideoById(videoId);


        //commentWebSocketHandler.sendNotification(videoId,videoService.getVideoCreator(videoId).getId(),commenter.getUserName(),video.getVideoName());
    }

    public VideoComment addComment(String commentText, UUID videoId,User commenter) {


        // Create a new VideoComment instance and set its properties
        VideoComment newComment = new VideoComment();
        newComment.setCommentText(commentText);
        newComment.setParentVideo(videoService.getVideoById(videoId));
        newComment.setCommenter(commenter);
        newComment.setCommentDateTime(LocalDateTime.now());
        // Save the new comment
        return videoCommentRepository.save(newComment);
    }


}
