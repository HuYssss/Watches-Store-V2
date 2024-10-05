package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.dto.category.response.CategoryResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.entity.Category;
import org.bson.types.ObjectId;

import java.util.List;

public interface CategoryService {
    Category findCategoryById(ObjectId id);
    List<CategoryResponse> getAllCategories();
    Category saveCategory(Category category);
    Category deleteCategory(ObjectId categoryId);
}
