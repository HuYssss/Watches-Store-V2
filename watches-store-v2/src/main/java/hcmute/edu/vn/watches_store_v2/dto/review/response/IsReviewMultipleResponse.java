package hcmute.edu.vn.watches_store_v2.dto.review.response;

import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsReviewMultipleResponse {
    private List<ReviewResponse> reviews = new ArrayList<ReviewResponse>();
    private ProductResponse product;

    public void addReview(ReviewResponse review) {
        reviews.add(review);
    }
}
