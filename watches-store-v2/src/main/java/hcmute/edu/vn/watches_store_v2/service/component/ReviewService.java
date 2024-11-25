package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import hcmute.edu.vn.watches_store_v2.entity.Review;
import org.bson.types.ObjectId;

import java.util.List;

public interface ReviewService {
    Review createReview(Review review, ObjectId userId);
    Review updateReview(Review review, ObjectId userId);
    Review deleteReview(ObjectId reviewId, ObjectId userId);
    List<Review> getReviewsByProduct(ObjectId productId);
}
