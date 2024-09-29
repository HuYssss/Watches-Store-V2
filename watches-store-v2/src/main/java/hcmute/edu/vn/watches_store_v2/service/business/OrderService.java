package hcmute.edu.vn.watches_store_v2.service.business;

import hcmute.edu.vn.watches_store_v2.dto.order.request.OrderRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllUserOrders(ObjectId userId);
    String createOrder(OrderRequest order, ObjectId userId);
    Order cancleOrder(ObjectId order, ObjectId userId, String message);
}
