package hcmute.edu.vn.watches_store_v2.service.business.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.productItem.request.ProductItemRequest;
import hcmute.edu.vn.watches_store_v2.dto.productItem.request.ProductItemUpdateRequest;
import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.entity.ProductItem;
import hcmute.edu.vn.watches_store_v2.mapper.ProductItemMapper;
import hcmute.edu.vn.watches_store_v2.service.business.CartService;
import hcmute.edu.vn.watches_store_v2.service.component.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductItemService productItemService;

    @Override
    public List<ProductItemResponse> getCart(ObjectId userId) {
        try {
            List<ProductItem> userItem = this.productItemService.findAllUserItem(userId);

            List<ObjectId> itemIds = userItem
                                        .stream()
                                        .map(ProductItem::getId)
                                        .collect(Collectors.toList());

            return this.productItemService.findAllByItemId(itemIds);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't get user's cart");
        }
    }

    @Override
    public ProductItemResponse addProductToCart(ObjectId userId, ProductItemRequest productItemRequest) {
        try {
            List<ProductItem> userItem = this.productItemService.findAllUserItem(userId);
            ProductItem item = userItem
                    .stream()
                    .filter(p -> p.getProduct().equals(productItemRequest.getProduct()))
                    .findFirst()
                    .orElse(null);

            if (item != null) {
                item.setQuantity(item.getQuantity() + productItemRequest.getQuantity());
                this.productItemService.saveProductItem(item);
                return ProductItemMapper.mapProductItemResp(item);
            } else {
                ProductItem productItem = ProductItemMapper.mapProductItemFromRequest(productItemRequest);
                productItem.setUser(userId);
                this.productItemService.saveProductItem(productItem);
                return ProductItemMapper.mapProductItemResp(productItem);
            }
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't add product to user's cart");
        }
    }

    @Override
    public ProductItem deleteProductFromCart(ObjectId userId, ObjectId itemId) {
        try {
            ProductItem productItem = this.productItemService.findItemById(itemId);

            if (productItem != null && productItem.getUser().equals(userId)) {
                this.productItemService.deleteProductItem(productItem.getId());
                return productItem;
            } else
                return null;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete product from user's cart");
        }
    }

    @Override
    public List<ProductItem> updateCart(ObjectId userId, List<ProductItemUpdateRequest> updateRequests) {
        try {
            List<ObjectId> deleteItem = new ArrayList<>();
            List<ProductItem> userItem = this.productItemService.findAllUserItem(userId);
            List<ProductItem> requestItem = updateRequests
                    .stream()
                    .map(u -> ProductItemMapper.mapProductItemFromUpdateReq(u))
                    .collect(Collectors.toList());

            for (ProductItem p1 : userItem) {
                boolean found = false;
                for (ProductItem p2 : requestItem) {
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

            this.productItemService.updateAll(userItem);
            this.productItemService.deleteAllById(deleteItem);

            return userItem;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update user's cart");
        }
    }


}
