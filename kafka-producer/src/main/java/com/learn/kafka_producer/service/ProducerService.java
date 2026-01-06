package com.learn.kafka_producer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, Product> kafkaTemplate;

    //Sending message asynchronously
    public String sendAsync(Product product) throws ExecutionException,
            InterruptedException {
        var result = kafkaTemplate.send("product-created-topic", product.getId(), product);
        result.whenComplete( (res, exception) -> {
           if(exception != null){
               log.error("Error occurred while publishing event with id : {}", product.getId());
           }
           else log.info("Successfully sent event");
        });
        return product.getId();
    }

    //sending message synchronously using producerRecord
    public Product sendSync(Product product) throws ExecutionException, InterruptedException {
        SendResult<String, Product> res =
                kafkaTemplate.send(createProducerRecord(product, null)).get();
        Product prod = res.getProducerRecord().value();
        log.info("{} message successfully sent to {}", prod.getName(), res.getRecordMetadata().topic());
        return prod;
    }

    public ProducerRecord<String, Product> createProducerRecord(Product product, String partition){
        return new ProducerRecord<>("product-created-topic", partition!=null? Integer.valueOf(partition):
                null ,
                product.getId(),
                product);
    }

    public Product sendToPartition(Product product, String partition) throws ExecutionException, InterruptedException {
        createProducerRecord(product, partition);
        SendResult<String, Product> res =
                kafkaTemplate.send(createProducerRecord(product, partition)).get();
        Product prod = res.getProducerRecord().value();
        log.info("{} message successfully sent to {}", prod.getName(), res.getRecordMetadata().topic());
        return prod;
    }

    public Product sendToTestTopic(String topic, String partition, Product product) throws ExecutionException, InterruptedException {
        ProducerRecord<String, Product> record = new ProducerRecord<>(topic,
                partition, product);
        return kafkaTemplate.send(record).get().getProducerRecord().value();
    }
}
