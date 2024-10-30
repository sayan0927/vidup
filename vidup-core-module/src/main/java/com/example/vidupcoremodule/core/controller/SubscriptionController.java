package com.example.vidupcoremodule.core.controller;



import com.example.vidupcoremodule.core.View_DTO.DTOService;
import com.example.vidupcoremodule.core.View_DTO.SubscriptionDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Subscription;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.service.userservices.SubscriptionService;
import com.example.vidupcoremodule.core.service.userservices.UserService;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {


    @Autowired
    UserService userService;
    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    UtilClass utilClass;

    @Autowired
    DTOService dtoService;



    @Autowired
    VideoService videoService;

    /**
     * Returns all subscriptions of logged in user IN JSON format
     * To get playlists in HTML VIEW,  Use endpoint /subscriptions/my/page
     * @param authentication    Authenticated User
     * @return                  Subscriptions of current user
     */
    @GetMapping("/my")
    public ResponseEntity<?> subscriptionsJSON(Authentication authentication) {


        List<SubscriptionDTO> subscriptionDTOS = dtoService.getUsersSubscriptionsDTO(utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        return new ResponseEntity<>(subscriptionDTOS, HttpStatus.OK);
    }

    /**
     * Returns all subscriptions of logged in user IN HTML view
     * To get playlists in JSON format,  Use endpoint /subscriptions/my
     * @param authentication    Authenticated User
     * @return                  Subscriptions of current user
     */
    @GetMapping("/my/page")
    public ModelAndView subscriptionsHTML(Authentication authentication) {
        LoggedInUserDetails loggedInUser = utilClass.getUserDetailsFromAuthentication(authentication);
        ModelAndView m = utilClass.getHTMLofLoggedInUser("subscriptions", loggedInUser);


        List<SubscriptionDTO> dto = dtoService.getUsersSubscriptionsDTO(loggedInUser.getUser());
        List<Video> videosUploaded = videoService.getUploadVideosOfUser(loggedInUser.getUser());
     //   List<VideoPageDTO> containers = dtoService.getContainersForVideos(videosUploaded);

        m.addObject("table_dto", dto);
       // m.addObject("current_user_videos_container", containers);
        m.setStatus(HttpStatusCode.valueOf(200));
        return m;
    }






    /**
     * Checks if the authenticated user is already subscribed to the specified user.
     * @param userId User ID to check subscription status
     * @param authentication Authenticated User
     * @return True if subscribed, false otherwise
     */
    @GetMapping("/subscribed_prev/{userId}")
    public ResponseEntity<?> subscribedAlready(@PathVariable("userId") String userId, Authentication authentication) {

        if (!userService.userIdExists(userId)) return new ResponseEntity<>("Invalid UserID", HttpStatus.NOT_FOUND);

        Boolean subscribed = subscriptionService.subscribedAlready(utilClass.getUserDetailsFromAuthentication(authentication).getUser(), userId);
        return new ResponseEntity<>(subscribed, HttpStatus.OK);
    }

    /**
     * Subscribes the authenticated user to the specified user.
     * @param userId User ID to subscribe to
     * @param authentication Authenticated User
     * @return Subscription details
     */
    @PostMapping("/subscribe/{userId}")
    public ResponseEntity<?> subscribe(@PathVariable("userId") String userId, Authentication authentication) {

        if (!userService.userIdExists(userId)) return new ResponseEntity<>("Invalid UserID", HttpStatus.NOT_FOUND);

        User subscriber = utilClass.getUserDetailsFromAuthentication(authentication).getUser();
        User subscribedTo = userService.getUserById(userId);


        if (subscribedTo.getId().equals(subscriber.getId())) {
            return new ResponseEntity<>("Cannot Subscribe to Yourself", HttpStatus.NOT_ACCEPTABLE);
        }


        Subscription subscription = subscriptionService.subscribe(utilClass.getUserDetailsFromAuthentication(authentication).getUser(), userId);
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    /**
     * Unsubscribes the authenticated user from the specified user.
     * @param userId User ID to unsubscribe from
     * @param authentication Authenticated User
     * @return True if unsubscribed, false otherwise
     */
    @DeleteMapping("/unsubscribe/{userId}")
    public ResponseEntity<?> unsubscribe(@PathVariable("userId") String userId, Authentication authentication) {

        if (!userService.userIdExists(userId)) return new ResponseEntity<>("Invalid UserID", HttpStatus.NOT_FOUND);

        Boolean unsubscribed = subscriptionService.unsubscribe(utilClass.getUserDetailsFromAuthentication(authentication).getUser(), userId);
        return new ResponseEntity<>(unsubscribed, HttpStatus.OK);
    }
}
