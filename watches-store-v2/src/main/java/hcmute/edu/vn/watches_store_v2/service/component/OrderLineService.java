package hcmute.edu.vn.watches_store_v2.service.component;


import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.entity.OrderLine;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderLineService {
    List<OrderLine> findAllUserItem(ObjectId user);
    OrderLine saveOrderLine(OrderLine orderLine);
    void deleteOrderLine(ObjectId itemId);
    void deleteAllById(List<ObjectId> itemIds);
    OrderLine findItemById(ObjectId itemId);
    OrderLine update(OrderLineUpdateRequest orderLineUpdateRequest);
    List<OrderLine> updateAll(List<OrderLine> orderLines);
    List<OrderLine> updateOrderItem(List<OrderLine> orderLines);
    List<OrderLineResponse> findAllByItemId(List<ObjectId> itemId);
}
