package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.review.request.ReviewRequest;
import hcmute.edu.vn.watches_store_v2.dto.review.response.ReviewResponse;
import hcmute.edu.vn.watches_store_v2.entity.Review;
import org.bson.types.ObjectId;

import java.util.Date;

public class ReviewMapper {
    public static Review mapReview(ReviewRequest reviewRequest) {
        return new Review(
                new ObjectId(),
                reviewRequest.getRating(),
                reviewRequest.getReviewText(),
                reviewRequest.getReviewImages(),
                new Date(),
                null,
                null,
                reviewRequest.getProductId()
        );
    }

    public static ReviewResponse mapReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId().toHexString(),
                review.getRating(),
                review.getReviewText(),
                review.getReviewImages(),
                review.getCreatedAt(),
                review.getUserId().toHexString(),
                review.getUser(),
                review.getProductId().toHexString()
        );
    }
}
