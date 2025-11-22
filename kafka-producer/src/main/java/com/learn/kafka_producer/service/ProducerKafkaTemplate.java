package com.learn.kafka_producer.service;

import com.learn.kafka_producer.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProducerKafkaTemplate {

    @Autowired
    private KafkaTemplate<Integer, Product> kafkaTemplate;

    public Integer sendKafkaTemplate(Product product) throws ExecutionException, InterruptedException {

        var result = kafkaTemplate.send("product-created-topic", product.getId(), product);
        result.whenComplete( (res, exception) -> {
           if(exception != null){
               log.error("Error occurred while publishing event with id : {}", product.getId());
           }
           else log.info("Successfully sent event");
        });
        return product.getId();
    }
}
