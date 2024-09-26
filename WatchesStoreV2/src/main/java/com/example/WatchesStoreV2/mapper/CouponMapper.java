package com.example.WatchesStoreV2.mapper;

import com.example.WatchesStoreV2.dto.coupon.request.CouponRequest;
import com.example.WatchesStoreV2.dto.coupon.response.CouponResponse;
import com.example.WatchesStoreV2.entity.Coupon;
import org.bson.types.ObjectId;

import java.util.Date;

public class CouponMapper {
    public static Coupon mapCoupon(CouponRequest coupon) {
        return new Coupon(
                new ObjectId(),
                coupon.getCouponName(),
                coupon.getCouponCode(),
                coupon.getDescription(),
                coupon.getDiscount(),
                new Date(),
                coupon.getExpiryDate(),
                coupon.getTimes(),
                coupon.getState(),
                coupon.getMinPrice(),
                coupon.getProvince()
        );
    }

    public static CouponResponse mapCouponResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId().toHexString(),
                coupon.getCouponName(),
                coupon.getCouponCode(),
                coupon.getDescription(),
                coupon.getDiscount(),
                coupon.getCreatedDate(),
                coupon.getExpiryDate(),
                coupon.getTimes(),
                coupon.getState(),
                coupon.getMinPrice(),
                coupon.getProvince()
        );
    }
}
