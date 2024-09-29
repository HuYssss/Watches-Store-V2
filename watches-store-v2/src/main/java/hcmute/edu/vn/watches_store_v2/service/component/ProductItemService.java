package hcmute.edu.vn.watches_store_v2.service.component;


import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.entity.ProductItem;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProductItemService {
    List<ProductItem> findAllUserItem(ObjectId user);
    ProductItem saveProductItem(ProductItem productItem);
    void deleteProductItem(ObjectId itemId);
    void deleteAllById(List<ObjectId> itemIds);
    ProductItem findItemById(ObjectId itemId);
    List<ProductItem> updateAll(List<ProductItem> productItems);
    List<ProductItem> updateOrderItem(List<ProductItem> productItems);
    List<ProductItemResponse> findAllByItemId(List<ObjectId> itemId);
}
