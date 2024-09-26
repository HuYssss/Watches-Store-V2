package com.example.WatchesStoreV2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ProductItem")
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    @Id
    private ObjectId id;

    private ObjectId product;

    private int quantity;

    private ObjectId user;

    private ObjectId order;
}
