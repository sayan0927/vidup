package com.example.vidupcoremodule.core.service.videoservices;



import com.example.vidupcoremodule.core.View_DTO.ReactionDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoReaction;
import com.example.vidupcoremodule.core.entity.composite_ids.VideoUserPrimaryKey;
import com.example.vidupcoremodule.core.repository.VideoReactionRepository;
import com.example.vidupcoremodule.core.repository.VideoRepository;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VideoReactionService {

    @Autowired
    VideoRepository videoRepository;
    @Autowired
    UtilClass utilClass;
    @Autowired
    private VideoReactionRepository videoReactionRepository;





    public Boolean isVideoLikedBy(UUID videoId, User likedBy) {

        Video video = videoRepository.findVideoById(videoId);
        VideoReaction existingReaction = videoReactionRepository.findVideoReactionByPrimaryKeyVideoAndPrimaryKeyUser(video,likedBy);
        return existingReaction != null && existingReaction.getScore().equals(1);
    }

    public Boolean isVideoDislikedBy(UUID videoId,User dislikedBy) {

        Video video = videoRepository.findVideoById(videoId);
        VideoReaction existingReaction = videoReactionRepository.findVideoReactionByPrimaryKeyVideoAndPrimaryKeyUser(video,dislikedBy);
        return existingReaction != null && existingReaction.getScore().equals(-1);
    }

    public VideoReaction neutralReactionOnVideo(UUID videoId,User dislikedBy) {

        Video video = videoRepository.findVideoById(videoId);

        VideoReaction reaction = videoReactionRepository.findVideoReactionByPrimaryKeyVideoAndPrimaryKeyUser(video,dislikedBy);

        if(reaction == null)
            return null;

        reaction.setScore(0);
        reaction.setLastTime(LocalDateTime.now());
        return videoReactionRepository.save(reaction);
    }



    public VideoReaction dislikeVideo(UUID videoId,User dislikedBy) {
        Video video = videoRepository.findVideoById(videoId);

        VideoReaction existing = videoReactionRepository.findVideoReactionByPrimaryKeyVideoAndPrimaryKeyUser(video,dislikedBy);

        if(existing != null)
        {
            existing.setScore(-1);
            return videoReactionRepository.save(existing);
        }
        else
        {
            VideoUserPrimaryKey pk = new VideoUserPrimaryKey();
            pk.setVideo(video);
            pk.setUser(dislikedBy);

            existing = new VideoReaction();
            existing.setScore(-1);
            existing.setLastTime(LocalDateTime.now());
            existing.setPrimaryKey(pk);

            return videoReactionRepository.save(existing);
        }
    }

    public ReactionDTO getAllReactionsCount(UUID videoId) {
        return new ReactionDTO(positiveReactions(videoId), negativeReactions(videoId));
    }

    public boolean previouslyReacted(UUID videoId, User reactingUser) {
        VideoReaction vr = videoReactionRepository.findVideoReactionByPrimaryKeyVideoAndPrimaryKeyUser(videoRepository.findVideoById(videoId), reactingUser);

        return vr != null;
    }

    public VideoReaction likeVideo(UUID videoId,User likedBy)
    {
        Video video = videoRepository.findVideoById(videoId);

        VideoReaction existing = videoReactionRepository.findVideoReactionByPrimaryKeyVideoAndPrimaryKeyUser(video,likedBy);

        if(existing != null)
        {
            existing.setScore(1);
            return videoReactionRepository.save(existing);
        }
        else
        {
            VideoUserPrimaryKey pk = new VideoUserPrimaryKey();
            pk.setVideo(video);
            pk.setUser(likedBy);

            existing = new VideoReaction();
            existing.setScore(1);
            existing.setLastTime(LocalDateTime.now());
            existing.setPrimaryKey(pk);

            return videoReactionRepository.save(existing);
        }

    }



    public int positiveReactions(Video video) {
        return videoReactionRepository.getLikesOnVideo(video.getId());
    }


    public int negativeReactions(Video video) {
        return videoReactionRepository.getDisLikesOnVideo(video.getId());
    }

    public int negativeReactions(UUID videoId) {
        return videoReactionRepository.getDisLikesOnVideo(videoId);
    }

    public int positiveReactions(UUID videoId) {
        return videoReactionRepository.getLikesOnVideo(videoId);
    }

}
