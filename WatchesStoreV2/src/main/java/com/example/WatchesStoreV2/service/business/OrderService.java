package com.example.WatchesStoreV2.service.business;

import com.example.WatchesStoreV2.dto.order.request.OrderRequest;
import com.example.WatchesStoreV2.dto.order.response.OrderResponse;
import com.example.WatchesStoreV2.entity.Order;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllUserOrders(ObjectId userId);
    String createOrder(OrderRequest order, ObjectId userId);
    Order cancleOrder(ObjectId order, ObjectId userId, String message);
}
