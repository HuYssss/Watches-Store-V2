package com.example.WatchesStoreV2.repository;

import com.example.WatchesStoreV2.entity.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findAllByUserId(ObjectId id);
}
