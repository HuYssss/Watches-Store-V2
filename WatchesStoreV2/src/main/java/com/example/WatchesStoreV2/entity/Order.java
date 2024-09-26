package com.example.WatchesStoreV2.entity;

import com.example.WatchesStoreV2.dto.coupon.response.CouponResponse;
import com.example.WatchesStoreV2.dto.productItem.response.ProductItemResponse;
import com.example.WatchesStoreV2.dto.user.response.ProfileOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private ObjectId id;

    private List<ProductItemResponse> products;

    private String paymentMethod;

    private double itemsPrice;

    private double shippingPrice;

    private double totalPrice;

    private ProfileOrder user;

    private boolean isPaid;

    private Date paidAt;

    private boolean isDelivered;

    private Date deliveredAt;

    private Date createdAt;

    private ObjectId userId;

    private CouponResponse coupon;

    private String state;

    private String cancelMessage;
}
