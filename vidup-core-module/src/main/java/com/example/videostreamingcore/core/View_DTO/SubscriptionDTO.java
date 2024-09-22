package com.example.videostreamingcore.core.View_DTO;

import com.example.videostreamingcore.core.entity.DatabaseEntities.User;

public record SubscriptionDTO(User subscribedTo,

                              int uploadedVideos,

                              int subscribersOfThisUser,

                              int totalViewsOfThisUser) {


}
