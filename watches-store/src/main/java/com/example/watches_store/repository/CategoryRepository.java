package com.example.watches_store.repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.watches_store.entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    
}
