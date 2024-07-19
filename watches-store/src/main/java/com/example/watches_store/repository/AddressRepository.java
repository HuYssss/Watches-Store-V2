package com.example.watches_store.repository;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.watches_store.entity.Address;

@Repository
public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    List<Address> findByUser(ObjectId user);
}
