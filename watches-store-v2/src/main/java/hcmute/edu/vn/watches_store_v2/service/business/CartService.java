package hcmute.edu.vn.watches_store_v2.service.business;

import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.entity.OrderLine;
import org.bson.types.ObjectId;

import java.util.List;

public interface CartService {
    List<OrderLineResponse> getCart(ObjectId userId);
    OrderLineResponse addProductToCart(ObjectId userId, OrderLineRequest orderLineRequest);
    OrderLine deleteProductFromCart(ObjectId userId, ObjectId itemId);
    List<OrderLine> updateCart(ObjectId userId, List<OrderLineUpdateRequest> itemUpdates);
    OrderLine deleteItemFromCart(ObjectId userId, ObjectId itemId);
    OrderLine deleteAllItemsFromCart(ObjectId userId);
}
