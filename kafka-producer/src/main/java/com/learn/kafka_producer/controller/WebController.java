package com.learn.kafka_producer.controller;

import com.learn.kafka_producer.model.Product;
import com.learn.kafka_producer.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/product")
public class WebController {
    @Autowired
    private ProducerService producerService;

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody Product product) throws ExecutionException, InterruptedException {
        producerService.sendKafkaTemplate(product);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }
}
