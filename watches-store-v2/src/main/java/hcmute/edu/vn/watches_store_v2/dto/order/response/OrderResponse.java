package hcmute.edu.vn.watches_store_v2.dto.order.response;


import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;

    private List<ProductItemResponse> orderItems;

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

    private String state;

    private String cancelMessage;
}
