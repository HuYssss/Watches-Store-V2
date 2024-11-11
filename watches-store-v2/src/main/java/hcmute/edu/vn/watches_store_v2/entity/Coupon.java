package hcmute.edu.vn.watches_store_v2.entity;

import hcmute.edu.vn.watches_store_v2.dto.coupon.ProvinceCoupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Coupon")
public class Coupon {

    @Id
    private ObjectId id;

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
