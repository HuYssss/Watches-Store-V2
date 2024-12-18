package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.coupon.request.CouponRequest;
import hcmute.edu.vn.watches_store_v2.dto.coupon.response.CouponResponse;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import org.bson.types.ObjectId;

import java.util.Date;

public class CouponMapper {
    public static Coupon mapCoupon(CouponRequest coupon) {
        return new Coupon(
                new ObjectId(),
                coupon.getCouponName(),
                coupon.getImg(),
                coupon.getImageType(),
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
                coupon.getImg(),
                coupon.getImageType(),
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
