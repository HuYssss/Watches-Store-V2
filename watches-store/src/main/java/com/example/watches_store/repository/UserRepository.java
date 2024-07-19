package com.example.watches_store.repository;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.watches_store.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
}
