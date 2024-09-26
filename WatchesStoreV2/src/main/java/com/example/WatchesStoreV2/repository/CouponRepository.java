package com.example.WatchesStoreV2.repository;

import com.example.WatchesStoreV2.entity.Coupon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, ObjectId> {
    Optional<Coupon> findByCouponCode(String couponCode);
}
