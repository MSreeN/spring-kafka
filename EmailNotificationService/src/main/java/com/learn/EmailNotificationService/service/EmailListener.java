package com.learn.EmailNotificationService.service;

import org.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "product-created-topic")
public class EmailListener {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler
    public void consumerMail(Product product){
        log.info(product.toString());
    }
}
