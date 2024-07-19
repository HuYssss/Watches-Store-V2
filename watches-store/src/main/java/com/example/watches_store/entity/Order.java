package com.example.watches_store.entity;


import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private ObjectId id;

    private String shippingAddress;

    private String paymentMethod;

    private double itemsPrice;

    private double shippingPrice;

    private double totalPrice;

    private ObjectId user;

    private boolean isPaid;

    private Date paidAt;

    private boolean isDelivered;

    private Date deliveredAt;

    private Date createdAt;

    private String state;
}
