package hcmute.edu.vn.watches_store_v2.service.business.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.entity.OrderLine;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.mapper.OrderLineMapper;
import hcmute.edu.vn.watches_store_v2.repository.OrderLineRepository;
import hcmute.edu.vn.watches_store_v2.service.business.CartService;
import hcmute.edu.vn.watches_store_v2.service.component.OrderLineService;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
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
    private final ProductService productService;
    private final OrderLineRepository orderLineRepository;

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

            ProductResponse productResponse = this.productService.getProductById(orderLineRequest.getProduct());

            if (item != null && item.getOption().equals(orderLineRequest.getOption())) {
                item.setQuantity(item.getQuantity() + orderLineRequest.getQuantity());
                this.orderLineService.saveOrderLine(item);
                OrderLineResponse resp = OrderLineMapper.mapOrderLineResp(item);
                resp.setProduct(productResponse);
                return resp;
            } else {
                OrderLine orderLine = OrderLineMapper.mapOrderLineFromRequest(orderLineRequest);
                orderLine.setUser(userId);
                this.orderLineService.saveOrderLine(orderLine);
                OrderLineResponse resp = OrderLineMapper.mapOrderLineResp(orderLine);
                resp.setProduct(productResponse);
                return resp;
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
    public List<OrderLineResponse> updateCart(ObjectId userId, List<OrderLineUpdateRequest> updateRequests) {
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
                        p1.setOption(p2.getOption());
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

            return this.orderLineService.findAllByItemId(userItem.stream().map(OrderLine::getId).collect(Collectors.toList()));

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update user's cart");
        }
    }

    @Override
    public OrderLine deleteItemFromCart(ObjectId userId, ObjectId itemId) {
        OrderLine orderLine = this.orderLineService.findItemById(itemId);

        if (orderLine == null)
            return null;

        this.orderLineService.deleteOrderLine(orderLine.getId());

        return orderLine;
    }

    @Override
    public OrderLine deleteAllItemsFromCart(ObjectId userId) {
        List<OrderLine> orderLineList = this.orderLineService.findAllUserItem(userId);

        this.orderLineRepository.deleteAll(orderLineList);

        return orderLineList.get(0);
    }

}
