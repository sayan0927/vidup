package com.example.vidupcoremodule.core.service.userservices;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.Subscription;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.composite_ids.UserUserPrimaryKey;
import com.example.vidupcoremodule.core.repository.SubscriptionRepository;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UtilClass utilClass;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;


    List<Subscription> getSubscriptionsDoneByUser(User user) {
        return subscriptionRepository.findSubscriptionByPrimaryKey_Subscriber(user);
    }

    public int subscriberCount(User creator) {
        return subscriptionRepository.countSubscriptionByPrimaryKey_SubscribedTo(creator);
    }

    public Boolean subscribedAlready(User subscriber,String subscribedToId) {

        if(userService.getUserById(subscribedToId) == null) {
            return Boolean.FALSE;
        }

        UserUserPrimaryKey primaryKey = new UserUserPrimaryKey();
        primaryKey.setSubscriber(subscriber);
        primaryKey.setSubscribedTo(userService.getUserById(subscribedToId));

        return subscriptionRepository.existsById(primaryKey);
    }

    public Subscription subscribe(User subscriber, String subscribedToId) {

        if(userService.getUserById(subscribedToId) == null) {
            return null;
        }

        UserUserPrimaryKey primaryKey = new UserUserPrimaryKey();
        primaryKey.setSubscriber(subscriber);
        primaryKey.setSubscribedTo(userService.getUserById(subscribedToId));

        Subscription subscription = new Subscription();
        subscription.setPrimaryKey(primaryKey);

        return subscriptionRepository.save(subscription);
    }

    public Boolean unsubscribe(User subscriber, String subscribedToId) {

        if(userService.getUserById(subscribedToId) == null) {
            return null;
        }

        UserUserPrimaryKey primaryKey = new UserUserPrimaryKey();
        primaryKey.setSubscriber(subscriber);
        primaryKey.setSubscribedTo(userService.getUserById(subscribedToId));

        subscriptionRepository.deleteById(primaryKey);
        return !subscriptionRepository.existsById(primaryKey);
    }



    /**
     * @param subscriber
     * @return List of all users that have 'subscriber' has subscribed to
     */
    public List<User> getUsersSubscribedTo(User subscriber) {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionByPrimaryKey_Subscriber(subscriber);

        List<User> userList = new ArrayList<>();

        for (Subscription subscription : subscriptions) {
            userList.add(subscription.getPrimaryKey().getSubscribedTo());
        }

        return userList;
    }

    /**
     * @param subscribedTo
     * @return List of all users that have subscribed to 'subscribedTo'
     */
    public List<User> getUsersSubscribedBy(User subscribedTo) {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionByPrimaryKey_SubscribedTo(subscribedTo);

        List<User> userList = new ArrayList<>();

        for (Subscription subscription : subscriptions) {
            userList.add(subscription.getPrimaryKey().getSubscriber());
        }

        return userList;
    }

    /**
     * @param videoId The video which contributed to subscription
     * @return Subscription object
     */
    public Subscription handleSubscriptionRequest(UUID videoId, User subscriber) {

        Video contributingVideo = videoService.getVideoById(videoId);
        User subscribedTo = contributingVideo.getCreator();

        UserUserPrimaryKey primaryKey = new UserUserPrimaryKey();
        primaryKey.setSubscriber(subscriber);
        primaryKey.setSubscribedTo(subscribedTo);


        /**
         * If previously NOT subscribed,create record and return
         * Else delete record and return null (Unsubscribing)
         */
        Subscription previousSub = subscriptionRepository.findSubscriptionByPrimaryKey(primaryKey);
        if (previousSub == null) {
            Subscription subscription = new Subscription();
            subscription.setPrimaryKey(primaryKey);
            return subscriptionRepository.save(subscription);
        } else {
            subscriptionRepository.delete(previousSub);
            return null;
        }
    }

    List<Subscription> getSubscriptionsMadeToUser(User user) {
        return subscriptionRepository.findSubscriptionByPrimaryKey_SubscribedTo(user);
    }
}
