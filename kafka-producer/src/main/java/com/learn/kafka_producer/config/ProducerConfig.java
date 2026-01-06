package com.learn.kafka_producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ProducerConfig {

//    public Map<String, Object> producerConfig(){
//        return Map.of(org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5",
//                org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
//                "true");
//    }
//
//    @Bean
//    ProducerFactory<String, Product> producerFactory(){
//        return new DefaultKafkaProducerFactory<>(producerConfig());
//    }
//
//    @Bean
//    KafkaTemplate<String, Product> kafkaTemplate(){
//        return new KafkaTemplate<>(producerFactory());
//    }

    @Bean
    public NewTopic topic(){
        return new NewTopic("product-created-topic", 3, (short)1)
                .configs(Map.of(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "3"));
    }

    @Bean
    public NewTopic testTopic(){
        return new NewTopic("test-topic", 2, (short)1);
    }
}
