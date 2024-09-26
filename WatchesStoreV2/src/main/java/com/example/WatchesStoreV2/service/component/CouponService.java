package com.example.WatchesStoreV2.service.component;

import com.example.WatchesStoreV2.dto.coupon.response.CouponResponse;
import com.example.WatchesStoreV2.entity.Coupon;
import org.bson.types.ObjectId;

import java.util.List;

public interface CouponService {
    Coupon saveCoupon(Coupon coupon);
    Coupon deleteCoupon(ObjectId couponId);
    Coupon findCouponByCouponCode(String couponCode);
    List<CouponResponse> getAllCoupons();
}
