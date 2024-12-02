package hcmute.edu.vn.watches_store_v2.service.business;

import hcmute.edu.vn.watches_store_v2.dto.order.request.BuyNowRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.request.CancelOrder;
import hcmute.edu.vn.watches_store_v2.dto.order.request.OrderRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderSuccessResponse;
import hcmute.edu.vn.watches_store_v2.dto.order.response.Statistic;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import org.bson.types.ObjectId;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllUserOrders(ObjectId userId, String state);
    OrderSuccessResponse createOrder(OrderRequest order, ObjectId userId);
    Order cancleOrder(ObjectId order, ObjectId userId, String message);

    Order isPaid(ObjectId order);
    OrderSuccessResponse buyNow(BuyNowRequest buyNowRequest, ObjectId userId) throws UnsupportedEncodingException;
    OrderResponse isOrderDelivered(ObjectId orderId, ObjectId userId);

    OrderResponse approvalOrder(ObjectId orderId);
    OrderResponse declineOrder(CancelOrder cancelOrder);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(ObjectId orderId);

    boolean isOrdered(ObjectId productId, ObjectId userId);
    Statistic statistic(ObjectId userId);
}
