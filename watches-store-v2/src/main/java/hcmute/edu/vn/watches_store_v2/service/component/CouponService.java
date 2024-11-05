package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.dto.coupon.request.CouponRequest;
import hcmute.edu.vn.watches_store_v2.dto.coupon.response.CouponResponse;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import org.bson.types.ObjectId;

import java.util.List;

public interface CouponService {
    Coupon saveCoupon(Coupon coupon);
    Coupon updateCoupon(Coupon coupon);
    Coupon deleteCoupon(ObjectId couponId);
    Coupon findCouponByCouponCode(String couponCode);
    List<CouponResponse> getAllCoupons();
}
