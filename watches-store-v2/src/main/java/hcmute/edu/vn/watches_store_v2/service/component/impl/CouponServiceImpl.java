package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.coupon.response.CouponResponse;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import hcmute.edu.vn.watches_store_v2.mapper.CouponMapper;
import hcmute.edu.vn.watches_store_v2.repository.CouponRepository;
import hcmute.edu.vn.watches_store_v2.service.component.CouponService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        try {
            this.couponRepository.save(coupon);
            return coupon;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't create coupon");
        }
    }

    @Override
    public Coupon deleteCoupon(ObjectId couponId) {
        try {
            Coupon coupon = this.couponRepository.findById(couponId).orElse(null);

            if (coupon != null)
                this.couponRepository.deleteById(couponId);

            return coupon;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete coupon");
        }
    }

    @Override
    public Coupon findCouponByCouponCode(String couponCode) {
        try {
            return this.couponRepository.findByCouponCode(couponCode).orElse(null);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find coupon");
        }
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        try {
            return this.couponRepository.findAll()
                    .stream()
                    .map(c -> CouponMapper.mapCouponResponse(c))
                    .collect(Collectors.toList());
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find coupon");
        }
    }
}
