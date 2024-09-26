package com.example.WatchesStoreV2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Category")
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private ObjectId id;
    private String categoryName;
}
