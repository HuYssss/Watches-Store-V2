package com.example.watches_store.repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.watches_store.entity.ProductItem;

@Repository
public interface ProductItemRepository extends MongoRepository<ProductItem, ObjectId> {
    
}
