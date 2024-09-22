package org.example.vidupmediaprocessing.upload_pipeline.kafka_pipeline;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;
import java.util.Map;

/**
 * Provides Configuration for Kafka Producer and creates the Kafka Topics
 */
//@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.broker.address}")
    private String kafkaBrokerIp;

    public static final String UPLOAD_TRANSCODE_TOPIC = "upload_transcode";
    public static final String HANDLE_MP4_UPON_NSFW_PASS_TOPIC = "handle_mp4_upon_nsfw_pass";
    public static final String NSFW_DETECT_TOPIC = "nsfw_detect";

    /*
    @Bean
    public NewTopic uploadTranscodeTopic() {
        return TopicBuilder.name(KafkaProducerConfig.UPLOAD_TRANSCODE_TOPIC)
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic handleMP4Topic() {
        return TopicBuilder.name(KafkaProducerConfig.HANDLE_MP4_UPON_NSFW_PASS_TOPIC)
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic nsfwDetectTopic() {
        return TopicBuilder.name(KafkaProducerConfig.NSFW_DETECT_TOPIC)
                .partitions(5)
                .replicas(1)
                .build();
    }



    @Bean
    public ProducerFactory<String, Object> producerFactoryStringKeyJsonValue() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokerIp);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        // Set the custom partitioner to round robin
        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,  RoundRobinPartitioner.class.getName());
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplateStringKeyJsonValue() {
        return new KafkaTemplate<>(producerFactoryStringKeyJsonValue());
    }

    /*
    @Bean
    public <T> ProducerFactory<String, AbstractPipelineMessage<T>> producerFactoryStringKeyGenericValue() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokerIp);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        // Set the custom partitioner to round robin
        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,  RoundRobinPartitioner.class.getName());
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public <T> KafkaTemplate<String, AbstractPipelineMessage<T>> kafkaTemplateStringKeyGenericValue() {
        return new KafkaTemplate<>(producerFactoryStringKeyGenericValue());
    }


     */








}
