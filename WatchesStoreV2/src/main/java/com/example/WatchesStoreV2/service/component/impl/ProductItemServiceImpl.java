package com.example.WatchesStoreV2.service.component.impl;

import com.example.WatchesStoreV2.dto.product.response.ProductResponse;
import com.example.WatchesStoreV2.dto.productItem.response.ProductItemResponse;
import com.example.WatchesStoreV2.entity.Product;
import com.example.WatchesStoreV2.entity.ProductItem;
import com.example.WatchesStoreV2.mapper.ProductItemMapper;
import com.example.WatchesStoreV2.repository.ProductItemRepository;
import com.example.WatchesStoreV2.service.component.ProductItemService;
import com.example.WatchesStoreV2.service.component.ProductService;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService {

    private final ProductItemRepository productItemRepository;
    private final ProductService productService;

    @Override
    public List<ProductItem> findAllUserItem(ObjectId user) {
        try {
            return this.productItemRepository.findAllByUser(user);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find user's item");
        }
    }

    @Override
    public ProductItem saveProductItem(ProductItem productItem) {
        try {
            if (productItem.getId() == null) {
                productItem.setId(new ObjectId());
            }

            this.productItemRepository.save(productItem);

            return productItem;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public void deleteProductItem(ObjectId itemId) {
        try {
            this.productItemRepository.deleteById(itemId);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public void deleteAllById(List<ObjectId> itemIds) {
        try {
            this.productItemRepository.deleteAllById(itemIds);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete items");
        }
    }

    @Override
    public ProductItem findItemById(ObjectId itemId) {
        try {
            return this.productItemRepository.findById(itemId).orElse(null);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public List<ProductItem> updateAll(List<ProductItem> productItems) {
        try {
            this.productItemRepository.saveAll(productItems);
            return productItems;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user's item");
        }
    }

    @Override
    public List<ProductItem> updateOrderItem(List<ProductItem> productItems) {
        try {
            this.productItemRepository.deleteAll(productItems);
            return productItems;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update order's item");
        }
    }

    @Override
    public List<ProductItemResponse> findAllByItemId(List<ObjectId> itemId) {
        try {

            List<ProductResponse> products = this.productService.getAllProductResp();

            List<ProductItem> userItem = this.productItemRepository.findAllById(itemId);

            List<ProductItemResponse> itemResp = new ArrayList<>();

            for (ProductItem item : userItem) {
                ProductItemResponse productItemResponse = ProductItemMapper.mapProductItemResp(item);

                Optional<ProductResponse> product = products
                        .stream()
                        .filter(p -> p.getId().equals(item.getProduct().toHexString()))
                        .findFirst();

                if (product.isPresent()) {
                    productItemResponse.setProduct(product.get());
                    itemResp.add(productItemResponse);
                }
            }

            return itemResp;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update order's item");
        }
    }
}
