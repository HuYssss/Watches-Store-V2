package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.entity.OrderLine;
import hcmute.edu.vn.watches_store_v2.mapper.OrderLineMapper;
import hcmute.edu.vn.watches_store_v2.repository.OrderLineRepository;
import hcmute.edu.vn.watches_store_v2.service.component.OrderLineService;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final ProductService productService;

    @Override
    public List<OrderLine> findAllUserItem(ObjectId user) {
        try {
            return this.orderLineRepository.findAllByUser(user);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find user's item");
        }
    }

    @Override
    public OrderLine saveOrderLine(OrderLine orderLine) {
        try {
            if (orderLine.getId() == null) {
                orderLine.setId(new ObjectId());
            }

            this.orderLineRepository.save(orderLine);

            return orderLine;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public void deleteOrderLine(ObjectId itemId) {
        try {
            this.orderLineRepository.deleteById(itemId);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public void deleteAllById(List<ObjectId> itemIds) {
        try {
            this.orderLineRepository.deleteAllById(itemIds);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete items");
        }
    }

    @Override
    public OrderLine findItemById(ObjectId itemId) {
        try {
            return this.orderLineRepository.findById(itemId).orElse(null);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public OrderLine update(OrderLineUpdateRequest orderLineUpdateRequest) {
        try {
            OrderLine orderLine = this.findItemById(orderLineUpdateRequest.getItemId());

            if (orderLine == null) { return null; }

            orderLine.setOption(orderLineUpdateRequest.getOption());
            orderLine.setQuantity(orderLineUpdateRequest.getQuantity());

            this.orderLineRepository.save(orderLine);

            return orderLine;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update user's item");
        }
    }

    @Override
    public List<OrderLine> updateAll(List<OrderLine> orderLines) {
        try {
            this.orderLineRepository.saveAll(orderLines);
            return orderLines;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public List<OrderLine> updateOrderItem(List<OrderLine> orderLines) {
        try {
            this.orderLineRepository.deleteAll(orderLines);
            return orderLines;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update order's item");
        }
    }

    @Override
    public List<OrderLineResponse> findAllByItemId(List<ObjectId> itemId) {
        try {

            List<ProductResponse> products = this.productService.getAllProductResp();

            List<OrderLine> userItem = this.orderLineRepository.findAllById(itemId);

            List<OrderLineResponse> itemResp = new ArrayList<>();

            for (OrderLine item : userItem) {
                OrderLineResponse orderLineResponse = OrderLineMapper.mapOrderLineResp(item);

                Optional<ProductResponse> product = products
                        .stream()
                        .filter(p -> p.getId().equals(item.getProduct().toHexString()))
                        .findFirst();

                if (product.isPresent()) {
                    orderLineResponse.setProduct(product.get());
                    itemResp.add(orderLineResponse);
                }
            }

            return itemResp;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update order's item");
        }
    }
}
