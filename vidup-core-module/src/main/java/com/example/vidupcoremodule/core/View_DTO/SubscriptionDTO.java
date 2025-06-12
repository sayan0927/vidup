package com.example.vidupcoremodule.core.View_DTO;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;

public record SubscriptionDTO(User subscribedTo,

                              int uploadedVideos,

                              int subscribersOfThisUser,

                              int totalViewsOfThisUser) {


}
