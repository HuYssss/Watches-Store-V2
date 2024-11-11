package hcmute.edu.vn.watches_store_v2.dto.coupon.request;

import hcmute.edu.vn.watches_store_v2.dto.coupon.ProvinceCoupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {

    private String couponName;

    private String img;

    private String couponCode;

    private String description;

    private double discount;

    private Date createdDate;

    private Date expiryDate;

    private int times;

    private String state;

    private double minPrice;

    private ProvinceCoupon province;
}
