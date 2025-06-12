package com.example.vidupcoremodule.upload_pipeline.exceptions;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component
public class RabbitExceptionHandler implements RabbitListenerErrorHandler {
    @Override
    public Object handleError(Message message, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {


        String fail = message.toString().concat(message1.toString()).concat(e.getMessage());

        return fail;

    }
}
