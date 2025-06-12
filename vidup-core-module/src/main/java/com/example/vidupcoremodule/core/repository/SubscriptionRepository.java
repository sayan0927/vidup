package com.example.vidupcoremodule.core.repository;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.Subscription;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.composite_ids.UserUserPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UserUserPrimaryKey> {


    /**
     * Return subscriptions where user is subscriber
     * @param user  Subscriber
     * @return      Subscriptions
     */
    List<Subscription> findSubscriptionByPrimaryKey_Subscriber(User user);

    /**
     * Return subscriptions where user is subscribedTo
     * @param user  SubscribedTo
     * @return      Subscriptions
     */
    List<Subscription> findSubscriptionByPrimaryKey_SubscribedTo(User user);

    Subscription findSubscriptionByPrimaryKey(UserUserPrimaryKey primaryKey);

    /**
     * Counts number of subscribers of a user
     * @param user  the user
     * @return      number of subscribers who has subscribed to this user
     */
    int countSubscriptionByPrimaryKey_SubscribedTo(User user);


}
