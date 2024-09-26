package com.example.WatchesStoreV2.service.business;

import com.example.WatchesStoreV2.dto.productItem.request.ProductItemRequest;
import com.example.WatchesStoreV2.dto.productItem.request.ProductItemUpdateRequest;
import com.example.WatchesStoreV2.dto.productItem.response.ProductItemResponse;
import com.example.WatchesStoreV2.entity.ProductItem;
import org.bson.types.ObjectId;

import java.util.List;

public interface CartService {
    List<ProductItemResponse> getCart(ObjectId userId);
    ProductItemResponse addProductToCart(ObjectId userId, ProductItemRequest productItemRequest);
    ProductItem deleteProductFromCart(ObjectId userId, ObjectId itemId);
    List<ProductItem> updateCart(ObjectId userId, List<ProductItemUpdateRequest> itemUpdates);
}
