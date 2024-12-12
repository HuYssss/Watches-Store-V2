package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.dto.review.response.IsReviewMultipleResponse;
import hcmute.edu.vn.watches_store_v2.dto.review.request.UpdateReviewRequest;
import hcmute.edu.vn.watches_store_v2.entity.Review;
import org.bson.types.ObjectId;

import java.util.List;

public interface ReviewService {
    Review createReview(Review review, ObjectId userId);
    Review updateReview(UpdateReviewRequest review, ObjectId userId);
    Review deleteReview(ObjectId reviewId);
    List<Review> getReviewsByProduct(ObjectId productId);
    List<Review> getAllReviews();

    List<IsReviewMultipleResponse> getReviewMultiples(ObjectId user, List<ObjectId> productIds);
}
