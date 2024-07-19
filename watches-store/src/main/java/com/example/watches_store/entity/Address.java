package com.example.watches_store.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    private ObjectId id;
    
    private String province;
    
    private String district;

    private String ward;

    private ObjectId user;
}