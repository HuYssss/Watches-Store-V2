package com.example.WatchesStoreV2.repository;

import com.example.WatchesStoreV2.entity.RefreshTokenEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshTokenEntity, ObjectId> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
    List<RefreshTokenEntity> findAllByRevoked(boolean revoked);
}
