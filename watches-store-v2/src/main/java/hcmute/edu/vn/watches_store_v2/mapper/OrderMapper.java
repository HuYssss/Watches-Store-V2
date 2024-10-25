package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.order.request.BuyNowRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.request.OrderRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderSuccessResponse;
import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.entity.Product;
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
                order.getUserId().toHexString(),
                order.getCoupon(),
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
                (coupon != null) ? CouponMapper.mapCouponResponse(coupon) : null,
                "processing",
                null
        );
    }

    public static OrderSuccessResponse mapOrderSuccessResp(Order order, String redirectUrl) {
        return new OrderSuccessResponse(
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
                order.getUserId().toHexString(),
                order.getCoupon(),
                order.getState(),
                order.getCancelMessage(),
                redirectUrl
        );
    }

    public static Order mapNewOrder(List<ProductItemResponse> items, BuyNowRequest buyNowRequest, Coupon coupon) {
        return new Order(
                new ObjectId(),
                items,
                buyNowRequest.getPaymentMethod(),
                0,
                buyNowRequest.getShippingPrice(),
                0,
                buyNowRequest.getProfile(),
                false,
                null,
                false,
                null,
                new Date(),
                null,
                (coupon != null) ? CouponMapper.mapCouponResponse(coupon) : null,
                "processing",
                null
        );
    }
}
