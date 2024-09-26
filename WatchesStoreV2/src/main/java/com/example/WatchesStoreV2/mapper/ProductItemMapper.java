package com.example.WatchesStoreV2.mapper;

import com.example.WatchesStoreV2.dto.productItem.request.ProductItemRequest;
import com.example.WatchesStoreV2.dto.productItem.request.ProductItemUpdateRequest;
import com.example.WatchesStoreV2.dto.productItem.response.ProductItemResponse;
import com.example.WatchesStoreV2.entity.ProductItem;
import org.bson.types.ObjectId;

import java.util.List;

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
}
