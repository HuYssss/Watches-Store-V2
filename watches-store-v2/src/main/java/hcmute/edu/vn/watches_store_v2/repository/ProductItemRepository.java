package hcmute.edu.vn.watches_store_v2.repository;

import hcmute.edu.vn.watches_store_v2.entity.ProductItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends MongoRepository<ProductItem, ObjectId> {
    List<ProductItem> findAllByUser(ObjectId userId);
}