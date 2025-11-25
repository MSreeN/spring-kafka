package com.learn.kafka_producer.model;

import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Data
public class Product {
    String id = UUID.randomUUID().toString();
    String title;
    String name;
    String quantity;
}
