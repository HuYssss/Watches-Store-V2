package hcmute.edu.vn.watches_store_v2.dto.review.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsReviewMultipleReq {
    private List<String> productIds;

    public List<ObjectId> getProductIds() {

        List<ObjectId> ids = new ArrayList<ObjectId>();

        for (String str : productIds) {
            ObjectId id = new ObjectId(str);
            ids.add(id);
        }

        return ids;
    }
}
