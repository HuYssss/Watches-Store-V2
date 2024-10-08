package hcmute.edu.vn.watches_store_v2.dto.category.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignCategoryRequest {
    private ObjectId categoryId;
    private ObjectId productId;
}
