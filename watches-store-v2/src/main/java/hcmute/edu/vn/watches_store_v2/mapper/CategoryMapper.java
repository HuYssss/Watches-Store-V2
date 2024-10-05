package hcmute.edu.vn.watches_store_v2.mapper;



import hcmute.edu.vn.watches_store_v2.dto.category.request.CategoryRequest;
import hcmute.edu.vn.watches_store_v2.dto.category.response.CategoryResponse;
import hcmute.edu.vn.watches_store_v2.entity.Category;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class CategoryMapper {
    public static CategoryResponse mapCategoryResp(Category category) {
        return new CategoryResponse(
                category.getId().toHexString(),
                category.getCategoryName(),
                new ArrayList<>()
        );
    }

    public static Category mapCategory(CategoryRequest category) {
        return new Category(
                new ObjectId(),
                category.getCategoryName()
        );
    }
}
