package com.learn.kafka_producer.controller;

import com.learn.kafka_producer.model.Product;
import com.learn.kafka_producer.service.ProducerKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController("/product")
public class WebController {
    @Autowired
    private ProducerKafkaTemplate kafkaTemplate;

    @PostMapping("/create")
    public ResponseEntity<Integer> createProduct(@RequestBody Product product) throws ExecutionException, InterruptedException {
        int result;
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }
}
