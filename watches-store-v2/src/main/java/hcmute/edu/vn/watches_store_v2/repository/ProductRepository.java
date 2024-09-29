package hcmute.edu.vn.watches_store_v2.repository;

import hcmute.edu.vn.watches_store_v2.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
}
