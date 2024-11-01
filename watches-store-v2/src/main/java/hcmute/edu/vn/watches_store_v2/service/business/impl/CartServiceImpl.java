package hcmute.edu.vn.watches_store_v2.service.business.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.entity.OrderLine;
import hcmute.edu.vn.watches_store_v2.mapper.OrderLineMapper;
import hcmute.edu.vn.watches_store_v2.service.business.CartService;
import hcmute.edu.vn.watches_store_v2.service.component.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final OrderLineService orderLineService;

    @Override
    public List<OrderLineResponse> getCart(ObjectId userId) {
        try {
            List<OrderLine> userItem = this.orderLineService.findAllUserItem(userId);

            List<ObjectId> itemIds = userItem
                                        .stream()
                                        .map(OrderLine::getId)
                                        .collect(Collectors.toList());

            return this.orderLineService.findAllByItemId(itemIds);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't get user's cart");
        }
    }

    @Override
    public OrderLineResponse addProductToCart(ObjectId userId, OrderLineRequest orderLineRequest) {
        try {
            List<OrderLine> userItem = this.orderLineService.findAllUserItem(userId);
            OrderLine item = userItem
                    .stream()
                    .filter(p -> p.getProduct().equals(orderLineRequest.getProduct()))
                    .findFirst()
                    .orElse(null);

            if (item != null) {
                item.setQuantity(item.getQuantity() + orderLineRequest.getQuantity());
                this.orderLineService.saveOrderLine(item);
                return OrderLineMapper.mapOrderLineResp(item);
            } else {
                OrderLine orderLine = OrderLineMapper.mapOrderLineFromRequest(orderLineRequest);
                orderLine.setUser(userId);
                this.orderLineService.saveOrderLine(orderLine);
                return OrderLineMapper.mapOrderLineResp(orderLine);
            }
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't add product to user's cart");
        }
    }

    @Override
    public OrderLine deleteProductFromCart(ObjectId userId, ObjectId itemId) {
        try {
            OrderLine orderLine = this.orderLineService.findItemById(itemId);

            if (orderLine != null && orderLine.getUser().equals(userId)) {
                this.orderLineService.deleteOrderLine(orderLine.getId());
                return orderLine;
            } else
                return null;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete product from user's cart");
        }
    }

    @Override
    public List<OrderLine> updateCart(ObjectId userId, List<OrderLineUpdateRequest> updateRequests) {
        try {
            List<ObjectId> deleteItem = new ArrayList<>();
            List<OrderLine> userItem = this.orderLineService.findAllUserItem(userId);
            List<OrderLine> requestItem = updateRequests
                    .stream()
                    .map(u -> OrderLineMapper.mapOrderLineFromUpdateReq(u))
                    .collect(Collectors.toList());

            for (OrderLine p1 : userItem) {
                boolean found = false;
                for (OrderLine p2 : requestItem) {
                    if (p1.getId().equals(p2.getId())) {
                        found = true;
                        p1.setQuantity(p2.getQuantity());
                        break;
                    }
                }
                if (found == false) {
                    deleteItem.add(p1.getId());
                }
            }

            this.orderLineService.updateAll(userItem);
            this.orderLineService.deleteAllById(deleteItem);

            return userItem;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update user's cart");
        }
    }


}
