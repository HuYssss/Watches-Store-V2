package hcmute.edu.vn.watches_store_v2.controller.admin;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.review.response.ReviewResponse;
import hcmute.edu.vn.watches_store_v2.entity.Review;
import hcmute.edu.vn.watches_store_v2.mapper.ReviewMapper;
import hcmute.edu.vn.watches_store_v2.service.component.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/review")
public class ManageReviewController extends ControllerBase {

    private final ReviewService reviewService;

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

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/get-all-review")
    public ResponseEntity<?> getAllReview() {
        try {
            List<Review> reviews = this.reviewService.getAllReviews();

            List<ReviewResponse> resp = reviews
                    .stream()
                    .map(ReviewMapper::mapReviewResponse)
                    .collect(Collectors.toList());

            return response(resp, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
