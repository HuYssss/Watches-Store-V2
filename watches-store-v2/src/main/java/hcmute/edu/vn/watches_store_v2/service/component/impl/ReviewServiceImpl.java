package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import hcmute.edu.vn.watches_store_v2.entity.Review;
import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.mapper.UserMapper;
import hcmute.edu.vn.watches_store_v2.repository.ReviewRepository;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
import hcmute.edu.vn.watches_store_v2.service.business.ProfileService;
import hcmute.edu.vn.watches_store_v2.service.component.ReviewService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public Review createReview(Review review, ObjectId userId) {
        try {
            User user = this.userRepository.findById(userId).orElse(null);

            if (user != null)
                review.setUser(UserMapper.mapProfileOrder(user));

            this.reviewRepository.save(review);
            return review;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save review");
        }
    }

    @Override
    public Review updateReview(Review review, ObjectId userId) {
        try {
            Review review1 = this.reviewRepository.findById(review.getId()).orElse(null);

            if (review.getRating() != 0)      review1.setRating(review.getRating());
            if (review.getReviewText() != null)      review1.setReviewText(review.getReviewText());
            if (review.getReviewImages() != null)      review1.setReviewImages(review.getReviewImages());

            this.reviewRepository.save(review);
            return review;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update review");
        }
    }

    @Override
    public Review deleteReview(ObjectId id, ObjectId userId) {
        try {
            Review review = this.reviewRepository.findById(id).orElse(null);

            this.reviewRepository.delete(review);
            return review;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete review");
        }
    }

    @Override
    public List<Review> getReviewsByProduct(ObjectId productId) {
        try {
            return this.reviewRepository.findByProductId(productId);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete review");
        }
    }
}
