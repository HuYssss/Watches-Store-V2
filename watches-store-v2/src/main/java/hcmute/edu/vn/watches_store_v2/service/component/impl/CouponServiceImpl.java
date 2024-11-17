package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.coupon.request.CouponRequest;
import hcmute.edu.vn.watches_store_v2.dto.coupon.response.CouponResponse;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import hcmute.edu.vn.watches_store_v2.mapper.CouponMapper;
import hcmute.edu.vn.watches_store_v2.repository.CouponRepository;
import hcmute.edu.vn.watches_store_v2.service.component.CouponService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public Coupon updateCoupon(Coupon couponReq) {
        Coupon coupon = this.couponRepository.findById(couponReq.getId()).orElse(null);
        if (coupon == null) {
            return null;
        }

        if (couponReq.getImg() != null) { coupon.setImg(couponReq.getImg()); }
        if (couponReq.getCouponName() != null) { coupon.setCouponName(couponReq.getCouponName()); }
        if (couponReq.getDescription() != null) { coupon.setDescription(couponReq.getDescription()); }
        if (couponReq.getDiscount() > 0) { coupon.setDiscount(couponReq.getDiscount()); }
        if (couponReq.getCreatedDate() != null) { coupon.setCreatedDate(couponReq.getCreatedDate()); }
        if (couponReq.getExpiryDate() != null) { coupon.setExpiryDate(couponReq.getExpiryDate()); }
        if (couponReq.getTimes() > 0 ) { coupon.setTimes(couponReq.getTimes()); }
        if (couponReq.getState() != null) { coupon.setState(couponReq.getState()); }
        if (couponReq.getMinPrice() > 0) { coupon.setMinPrice(couponReq.getMinPrice()); }
        if (couponReq.getProvince() != null) { coupon.setProvince(couponReq.getProvince()); }


        try {
            this.couponRepository.save(coupon);
            return coupon;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update coupon");
        }
    }

    @Override
    public Coupon deleteCoupon(ObjectId couponId) {
        try {
            Coupon coupon = this.couponRepository.findById(couponId).orElse(null);

            if (coupon != null)
            {
                coupon.setState("deleted");
                this.couponRepository.save(coupon);
            }

            return coupon;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete coupon");
        }
    }

    @Override
    public Coupon findCouponByCouponCode(String couponCode) {
        try {
            if (couponCode == null)
                return null;

            return this.couponRepository.findByCouponCode(couponCode).orElse(null);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find coupon");
        }
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        try {

            List<Coupon> coupons = this.couponRepository.findAll();

            return this.couponRepository.findAll()
                    .stream()
//                    .filter(coupon -> coupon.getExpiryDate().after(new Date()) && coupon.getState().equals("active"))
                    .map(c -> CouponMapper.mapCouponResponse(c))
                    .collect(Collectors.toList());
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find coupon");
        }
    }

    @Override
    public CouponResponse getCouponById(ObjectId couponId) {
        try {
            Coupon coupon = this.couponRepository.findById(couponId).orElse(null);
            if (coupon == null)
               return null;
            return CouponMapper.mapCouponResponse(coupon);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find coupon");
        }
    }

    @Override
    public CouponResponse activeCoupon(ObjectId couponId) {
        try {
            Coupon coupon = this.couponRepository.findById(couponId).orElse(null);
            if (coupon == null)
                return null;

            coupon.setState("active");
            this.couponRepository.save(coupon);
            return CouponMapper.mapCouponResponse(coupon);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't active coupon");
        }
    }

    @Override
    public CouponResponse inactiveCoupon(ObjectId couponId) {
        try {
            Coupon coupon = this.couponRepository.findById(couponId).orElse(null);
            if (coupon == null)
                return null;

            coupon.setState("inactive");
            this.couponRepository.save(coupon);
            return CouponMapper.mapCouponResponse(coupon);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't inactive coupon");
        }
    }
}
