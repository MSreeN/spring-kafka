package com.learn.EmailNotificationService.config;

import com.learn.EmailNotificationService.exception.RetryableException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

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

    DefaultErrorHandler errorHandler(KafkaTemplate<String, Product> kafkaTemplate){
        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (record, ex) ->{
                            if(ex instanceof RecoverableDataAccessException){
                                return new TopicPartition("test-topic-retry",record.partition());
                            }
                            else{
                                return new TopicPartition("test-topic-dlt", record.partition());
                            }
                        });
        FixedBackOff backOff = new FixedBackOff(3000, 3);
        var errorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer);
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.info("{} message failed to consume on {} attempt - {}", record.value(),
                    deliveryAttempt, ex.getMessage());
        });
        errorHandler.addRetryableExceptions(RuntimeException.class);
        return errorHandler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Product> testCustomFactory(KafkaTemplate<String, Product> kafkaTemplate){
        ConcurrentKafkaListenerContainerFactory<String, Product> container =
                new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory());
        container.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return container;
    }

    public ProducerFactory<String, Product> producerFactory(){
        Map<String, Object> map = new HashMap<>();
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(map);
    }

    @Bean
    public KafkaTemplate<String, Product> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
