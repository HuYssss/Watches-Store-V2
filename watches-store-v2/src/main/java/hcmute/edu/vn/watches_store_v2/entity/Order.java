package hcmute.edu.vn.watches_store_v2.entity;

import hcmute.edu.vn.watches_store_v2.dto.coupon.response.CouponResponse;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
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

    private List<OrderLineResponse> products;

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
