package com.example.WatchesStoreV2.repository;

import com.example.WatchesStoreV2.entity.ProductItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductItemRepository extends MongoRepository<ProductItem, ObjectId> {
    List<ProductItem> findAllByUser(ObjectId userId);
}