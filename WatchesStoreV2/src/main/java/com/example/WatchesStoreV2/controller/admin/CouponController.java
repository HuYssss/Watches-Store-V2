package com.example.WatchesStoreV2.controller.admin;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.dto.coupon.request.CouponRequest;
import com.example.WatchesStoreV2.entity.Coupon;
import com.example.WatchesStoreV2.mapper.CouponMapper;
import com.example.WatchesStoreV2.service.component.CouponService;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/coupon")
@RequiredArgsConstructor
public class CouponController extends ControllerBase {

    private final CouponService couponService;

//    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("create")
    public ResponseEntity<?> createCoupon(@RequestBody CouponRequest coupon) {
        try {
            return response(this.couponService.saveCoupon(CouponMapper.mapCoupon(coupon))
                    , HttpStatus.CREATED);
        } catch (MongoException e) {
            e.printStackTrace();
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable ObjectId id, @RequestBody CouponRequest couponReq) {
        try {
            Coupon coupon = CouponMapper.mapCoupon(couponReq);
            coupon.setId(id);

            return response(this.couponService.saveCoupon(coupon)
                    , HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable ObjectId id) {
        try {
            return response(this.couponService.deleteCoupon(id)
                    , HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
