package com.example.WatchesStoreV2.mapper;

import com.example.WatchesStoreV2.dto.category.response.CategoryResponse;
import com.example.WatchesStoreV2.entity.Category;

import java.util.ArrayList;

public class CategoryMapper {
    public static CategoryResponse mapCategoryResp(Category category) {
        return new CategoryResponse(
                category.getId().toHexString(),
                category.getCategoryName(),
                new ArrayList<>()
        );
    }
}
