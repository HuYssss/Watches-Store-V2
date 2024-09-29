package hcmute.edu.vn.watches_store_v2.service.business;

import hcmute.edu.vn.watches_store_v2.dto.productItem.request.ProductItemRequest;
import hcmute.edu.vn.watches_store_v2.dto.productItem.request.ProductItemUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.entity.ProductItem;
import org.bson.types.ObjectId;

import java.util.List;

public interface CartService {
    List<ProductItemResponse> getCart(ObjectId userId);
    ProductItemResponse addProductToCart(ObjectId userId, ProductItemRequest productItemRequest);
    ProductItem deleteProductFromCart(ObjectId userId, ObjectId itemId);
    List<ProductItem> updateCart(ObjectId userId, List<ProductItemUpdateRequest> itemUpdates);
}
