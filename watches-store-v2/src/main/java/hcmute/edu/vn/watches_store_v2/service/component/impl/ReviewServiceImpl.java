package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.review.request.UpdateReviewRequest;
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

            if (user != null) {
                review.setUser(UserMapper.mapProfileOrder(user));
                review.setUserId(userId);
            }

            this.reviewRepository.save(review);
            return review;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save review");
        }
    }

    @Override
    public Review updateReview(UpdateReviewRequest review, ObjectId userId) {
        try {
            Review r = this.reviewRepository.findById(review.getId()).orElse(null);

            if (r == null || !r.getUserId().equals(userId))          return null;

            if (review.getRating() != 0)      r.setRating(review.getRating());
            if (review.getReviewText() != null)      r.setReviewText(review.getReviewText());
            if (review.getReviewImages() != null)      r.setReviewImages(review.getReviewImages());

            this.reviewRepository.save(r);
            return r;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update review");
        }
    }

    @Override
    public Review deleteReview(ObjectId id) {
        try {
            Review review = this.reviewRepository.findById(id).orElse(null);

            if (review == null)             return null;

            review.setDelete(true);

            this.reviewRepository.save(review);

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

    @Override
    public List<Review> getAllReviews() {
        try {
            return this.reviewRepository.findAll();
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete review");
        }
    }
}
