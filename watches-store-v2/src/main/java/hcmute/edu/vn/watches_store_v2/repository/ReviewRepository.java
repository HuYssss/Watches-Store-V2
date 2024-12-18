package hcmute.edu.vn.watches_store_v2.repository;

import hcmute.edu.vn.watches_store_v2.entity.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByProductId(ObjectId productId);
    List<Review> findByUserId(ObjectId userId);
}
