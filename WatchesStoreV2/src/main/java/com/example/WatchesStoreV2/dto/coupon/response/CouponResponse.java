package com.example.WatchesStoreV2.dto.coupon.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {

    private String id;

    private String couponName;

    private String couponCode;

    private String description;

    private double discount;

    private Date createdDate;

    private Date expiryDate;

    private int times;

    private String state;

    private double minPrice;

    private String province;

}
