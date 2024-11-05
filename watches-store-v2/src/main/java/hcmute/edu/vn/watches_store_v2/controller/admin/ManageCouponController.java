package hcmute.edu.vn.watches_store_v2.controller.admin;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.coupon.request.CouponRequest;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import hcmute.edu.vn.watches_store_v2.mapper.CouponMapper;
import hcmute.edu.vn.watches_store_v2.service.component.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/coupon")
public class ManageCouponController extends ControllerBase {

    private final CouponService couponService;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/get-all-coupon")
    public ResponseEntity<?> getAllCoupon() {
        try {
            return response(
                    this.couponService.getAllCoupons(),
                    HttpStatus.OK
            );
        } catch (MongoException e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/create")
    public ResponseEntity<?> createCoupon(@RequestBody CouponRequest couponRequest) {
        if (couponRequest.getState() == null)   couponRequest.setState("active");

        try {
            return response(
                    this.couponService.saveCoupon(CouponMapper.mapCoupon(couponRequest)),
                    HttpStatus.OK
            );
        } catch (MongoException e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/update")
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon couponRequest) {
        try {
            return response(
                    this.couponService.updateCoupon(couponRequest),
                    HttpStatus.OK
            );
        } catch (MongoException e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCoupon(@RequestParam ObjectId couponId) {
        try {
            return response(
                    this.couponService.deleteCoupon(couponId),
                    HttpStatus.OK
            );
        } catch (MongoException e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
