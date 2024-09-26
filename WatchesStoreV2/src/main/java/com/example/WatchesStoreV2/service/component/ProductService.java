package com.example.WatchesStoreV2.service.component;

import com.example.WatchesStoreV2.dto.product.response.PageResponse;
import com.example.WatchesStoreV2.dto.product.response.ProductResponse;
import com.example.WatchesStoreV2.entity.Product;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProductResp();
    ProductResponse getProductById(ObjectId id);
    Product saveProduct(Product product);
    Product deleteProduct(ObjectId product);
    Product assignCategory(ObjectId productId, ObjectId categoryId);
    PageResponse getAllProduct(String gender, String wireMaterial
            , String shape, String waterProof, String sortBy, String q
            , double minPrice, double maxPrice, int pageNum);
}
