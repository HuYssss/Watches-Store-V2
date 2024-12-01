package hcmute.edu.vn.watches_store_v2.controller;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.review.request.ReviewRequest;
import hcmute.edu.vn.watches_store_v2.dto.review.request.UpdateReviewRequest;
import hcmute.edu.vn.watches_store_v2.entity.Review;
import hcmute.edu.vn.watches_store_v2.mapper.ReviewMapper;
import hcmute.edu.vn.watches_store_v2.service.component.ReviewService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/review")
@RequiredArgsConstructor
public class ReviewController extends ControllerBase {
    private final ReviewService reviewService;

    @PostMapping("/create-review")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest reviewRequest, Principal principal) {

        Review review = ReviewMapper.mapReview(reviewRequest);

        try {
            this.reviewService.createReview(review, findIdByUsername(principal.getName()));
            return response(review, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-review")
    public ResponseEntity<?> updateReview(@RequestBody UpdateReviewRequest review, Principal principal) {
         try {
             this.reviewService.updateReview(review, findIdByUsername(principal.getName()));
             return response(review, HttpStatus.OK);
         } catch (Exception e) {
             return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

    @DeleteMapping("/delete-review")
    public ResponseEntity<?> deleteReview(@RequestParam ObjectId reviewId) {
        try {
            this.reviewService.deleteReview(reviewId);
            return response(null, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("/admin-delete")
    public ResponseEntity<?> adminDelete(@RequestParam ObjectId reviewId) {
        try {
            this.reviewService.deleteReview(reviewId);
            return response(null, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
