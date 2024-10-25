package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.dto.productItem.request.ProductItemRequest;
import hcmute.edu.vn.watches_store_v2.dto.productItem.request.ProductItemUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.entity.ProductItem;
import org.bson.types.ObjectId;

public class ProductItemMapper {
    public static ProductItemResponse mapProductItemResp(ProductItem productItem) {
        return new ProductItemResponse(
                productItem.getId().toString(),
                null,
                productItem.getQuantity()
        );
    }

    public static ProductItem mapProductItemFromResp(ProductItemResponse productItemResponse) {
        return new ProductItem(
                new ObjectId(productItemResponse.getId()),
                new ObjectId(productItemResponse.getProduct().getId()),
                productItemResponse.getQuantity(),
                null,
                null
        );
    }

    public static ProductItem mapProductItemFromRequest(ProductItemRequest productItemRequest) {
        return new ProductItem(
                new ObjectId(),
                productItemRequest.getProduct(),
                productItemRequest.getQuantity(),
                null,
                null
        );
    }

    public static ProductItem mapProductItemFromUpdateReq(ProductItemUpdateRequest updateRequest) {
        return new ProductItem(
                updateRequest.getItemId(),
                null,
                updateRequest.getQuantity(),
                null,
                null
        );
    }

    public static ProductItemResponse mapNewResponse(ProductResponse productResponse, int quantity) {
        ObjectId id = new ObjectId();

        return new ProductItemResponse(
                id.toHexString(),
                productResponse,
                quantity
        );
    }
}
