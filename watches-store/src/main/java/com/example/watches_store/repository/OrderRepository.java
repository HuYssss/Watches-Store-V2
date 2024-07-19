package com.example.watches_store.repository;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.watches_store.entity.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findByUser(ObjectId user);
}
