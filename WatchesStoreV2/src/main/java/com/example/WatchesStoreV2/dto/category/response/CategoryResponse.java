package com.example.WatchesStoreV2.dto.category.response;

import com.example.WatchesStoreV2.dto.product.response.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String id;
    private String categoryName;
    private List<ProductResponse> products = new ArrayList<>();

    public void setProduct(ProductResponse product) {
        this.products.add(product);
    }
}
