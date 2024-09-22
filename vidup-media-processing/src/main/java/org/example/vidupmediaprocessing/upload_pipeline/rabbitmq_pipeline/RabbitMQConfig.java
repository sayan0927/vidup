package org.example.vidupmediaprocessing.upload_pipeline.rabbitmq_pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbit.transcode.queue}")
    public String transcodeQueue;
    @Value("${rabbit.nsfw.queue}")
    public String nsfwQueue;
    @Value("${rabbit.dash.queue}")
    public String dashQueue;
    @Value("${rabbit.mp4.queue}")
    public String mp4Queue;
    @Value("${rabbit.finalize.queue}")
    public String finalizeQueue;
    @Value("${rabbit.fail.queue}")
    public String failQueue;

    @Value("${rabbit.pipeline.exchange}")
    public String pipelineExchange;

    @Value("${rabbit.transcode.routing}")
    public String transcodeKey;
    @Value("${rabbit.nsfw.routing}")
    public String nsfwKey;
    @Value("${rabbit.dash.routing}")
    public String dashKey;
    @Value("${rabbit.mp4.routing}")
    public String mp4Key;
    @Value("${rabbit.finalize.routing}")
    public String finalizeKey;
    @Value("${rabbit.fail.routing}")
    public String failKey;





    @Bean
    public Queue transcodeQueue()
    {
        return new Queue(transcodeQueue, true);
    }

    @Bean
    public Queue finalizeQueue()
    {
        return new Queue(finalizeQueue, true);
    }

    @Bean
    public Queue mp4Queue()
    {
        return new Queue(mp4Queue, true);
    }

    @Bean
    public Queue nsfwQueue()
    {
        return new Queue(nsfwQueue, true);
    }

    @Bean
    public Queue dashQueue()
    {
        return new Queue(dashQueue, true);
    }

    @Bean
    public Queue failQueue()
    {
        return new Queue(failQueue, true);
    }



    @Bean
    public Exchange pipeLineExchange()
    {
        DirectExchange exchange = new DirectExchange(pipelineExchange);
        return exchange;

    }


    @Bean
    public Binding transcodeBinding()
    {
        return BindingBuilder.bind(transcodeQueue())
                .to(pipeLineExchange())
                .with(transcodeKey)
                .noargs();
    }

    @Bean
    public Binding finalizeBinding()
    {
        return BindingBuilder.bind(finalizeQueue())
                .to(pipeLineExchange())
                .with(finalizeKey)
                .noargs();
    }

    @Bean
    public Binding nsfwBinding()
    {
        return BindingBuilder.bind(nsfwQueue())
                .to(pipeLineExchange())
                .with(nsfwKey)
                .noargs();
    }

    @Bean
    public Binding dashBinding()
    {
        return BindingBuilder.bind(dashQueue())
                .to(pipeLineExchange())
                .with(dashKey)
                .noargs();
    }

    @Bean
    public Binding mp4Binding()
    {
        return BindingBuilder.bind(mp4Queue())
                .to(pipeLineExchange())
                .with(mp4Key)
                .noargs();
    }


    @Bean
    public Binding failBinding()
    {
        return BindingBuilder.bind(failQueue())
                .to(pipeLineExchange())
                .with(failKey)
                .noargs();
    }





    @Bean
    public MessageConverter messageConverter(ObjectMapper jsonMapper) {



        return new Jackson2JsonMessageConverter(jsonMapper);
    }
}