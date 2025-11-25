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

    @PostMapping("/create/async")
    public ResponseEntity<String> createProductAsync(@RequestBody Product product) throws ExecutionException, InterruptedException {
        producerService.sendAsync(product);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }

    @PostMapping("/create/sync")
    public ResponseEntity<Product> createProductSync(@RequestBody Product product) throws ExecutionException, InterruptedException {
        producerService.sendSync(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
