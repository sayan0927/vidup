package com.example.vidupcoremodule.events;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public void publishEvent(AbstractEvent event) {

        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void publishGenericEvent(GenericEvent event) {

        eventPublisher.publishEvent(event);

    }
}
