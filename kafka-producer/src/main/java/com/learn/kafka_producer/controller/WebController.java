package com.learn.kafka_producer.controller;

import com.learn.kafka_producer.service.ProducerService;
import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create/{partition}")
    public ResponseEntity<Product> createProductWithPartition(@PathVariable String partition,
                                                              @RequestBody Product product) throws ExecutionException, InterruptedException {
        producerService.sendToPartition(product, partition);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
