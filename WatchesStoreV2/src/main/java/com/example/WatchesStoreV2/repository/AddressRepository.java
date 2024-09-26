package com.example.WatchesStoreV2.repository;

import com.example.WatchesStoreV2.entity.Address;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    List<Address> findByUser(ObjectId user);
}
