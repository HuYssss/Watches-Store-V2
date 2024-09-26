package com.example.WatchesStoreV2.service.component;

import com.example.WatchesStoreV2.dto.category.response.CategoryResponse;
import com.example.WatchesStoreV2.entity.Category;
import org.bson.types.ObjectId;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    Category saveCategory(Category category);
    Category deleteCategory(ObjectId categoryId);
}
