package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.orderLine.OrderLineDetail;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.request.OrderLineUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.entity.OrderLine;
import org.bson.types.ObjectId;

public class OrderLineMapper {
    public static OrderLineResponse mapOrderLineResp(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId().toString(),
                null,
                orderLine.getQuantity(),
                orderLine.getOption()
        );
    }

    public static OrderLine mapOrderLineFromResp(OrderLineResponse orderLineResponse) {
        return new OrderLine(
                new ObjectId(orderLineResponse.getId()),
                new ObjectId(orderLineResponse.getProduct().getId()),
                orderLineResponse.getQuantity(),
                orderLineResponse.getOption(),
                null,
                null
        );
    }

    public static OrderLine mapOrderLineFromRequest(OrderLineRequest orderLineRequest) {
        return new OrderLine(
                new ObjectId(),
                orderLineRequest.getProduct(),
                orderLineRequest.getQuantity(),
                orderLineRequest.getOption(),
                null,
                null
        );
    }

    public static OrderLine mapOrderLineFromUpdateReq(OrderLineUpdateRequest updateRequest) {
        return new OrderLine(
                updateRequest.getItemId(),
                null,
                updateRequest.getQuantity(),
                updateRequest.getOption(),
                null,
                null
        );
    }

    public static OrderLineResponse mapNewResponse(ProductResponse productResponse, int quantity, String option) {
        ObjectId id = new ObjectId();

        return new OrderLineResponse(
                id.toHexString(),
                productResponse,
                quantity,
                option
        );
    }

    public static OrderLineDetail mapOrderLineDetail(OrderLineResponse orderLineResponse) {
        return new OrderLineDetail(
                orderLineResponse.getId(),
                ProductMapper.mapProductOrder(orderLineResponse),
                orderLineResponse.getQuantity()
        );
    }
}
