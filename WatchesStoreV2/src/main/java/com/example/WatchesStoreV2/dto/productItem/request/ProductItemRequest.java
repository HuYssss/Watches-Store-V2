package com.example.WatchesStoreV2.dto.productItem.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemRequest {
    private ObjectId product;
    private int quantity;
}
