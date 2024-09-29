package hcmute.edu.vn.watches_store_v2.repository;

import hcmute.edu.vn.watches_store_v2.entity.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findAllByUserId(ObjectId id);
}