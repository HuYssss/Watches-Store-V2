package hcmute.edu.vn.watches_store_v2.repository;

import hcmute.edu.vn.watches_store_v2.entity.RefreshToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {
    Optional<RefreshToken> findByRefreshToken(String token);
}
