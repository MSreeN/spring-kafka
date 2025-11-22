package com.learn.kafka_producer.model;

import lombok.Data;

@Data
public class Product {
    int id;
    String title;
    String name;
    String quantity;
}
