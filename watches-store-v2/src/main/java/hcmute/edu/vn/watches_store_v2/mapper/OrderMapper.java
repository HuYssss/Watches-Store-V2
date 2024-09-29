package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.order.request.OrderRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class OrderMapper {
    public static OrderResponse mapOrderResp(Order order) {
        return new OrderResponse(
                order.getId().toHexString(),
                order.getProducts(),
                order.getPaymentMethod(),
                order.getItemsPrice(),
                order.getShippingPrice(),
                order.getTotalPrice(),
                order.getUser(),
                order.isPaid(),
                order.getPaidAt(),
                order.isDelivered(),
                order.getDeliveredAt(),
                order.getCreatedAt(),
                order.getState(),
                order.getCancelMessage()
        );
    }

    public static Order mapOrder(List<ProductItemResponse> items, ProfileOrder profile, OrderRequest orderRequest, Coupon coupon) {
        return new Order(
                new ObjectId(),
                items,
                orderRequest.getPaymentMethod(),
                0,
                orderRequest.getShippingPrice(),
                0,
                profile,
                false,
                null,
                false,
                null,
                new Date(),
                null,
                CouponMapper.mapCouponResponse(coupon),
                "processing",
                null
        );
    }
}
