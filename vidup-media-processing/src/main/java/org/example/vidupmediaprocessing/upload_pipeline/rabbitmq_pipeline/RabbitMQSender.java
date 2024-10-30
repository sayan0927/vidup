package org.example.vidupmediaprocessing.upload_pipeline.rabbitmq_pipeline;


import org.example.vidupmediaprocessing.upload_pipeline.pipeline_messages.AbstractPipelineMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {


    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(AbstractPipelineMessage<?> message, String exchange, String routingKey) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);

    }
}
