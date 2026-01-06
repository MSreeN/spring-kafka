package com.learn.EmailNotificationService.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerContainerConfig {

    Logger log = LoggerFactory.getLogger(ConsumerContainerConfig.class);

    public ConsumerFactory<String, Product> consumerFactory(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);
        configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "email-group");
        configs.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "org.example.model");
        return new DefaultKafkaConsumerFactory(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Product> testCustomFactory(){
        ConcurrentKafkaListenerContainerFactory<String, Product> container =
                new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory());
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((record, exception) -> {
            log.error("{{}} occurred during deserialization of {} event", exception.getMessage(),
                    record.topic());
        });
        container.setCommonErrorHandler(errorHandler);
        return container;
    }
}
