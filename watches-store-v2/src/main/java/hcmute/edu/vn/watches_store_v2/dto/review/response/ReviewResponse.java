package hcmute.edu.vn.watches_store_v2.dto.review.response;

import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private String id;
    private int rating;
    private String reviewText;
    private List<String> reviewImages;
    private Date createdAt;
    private String userId;
    private ProfileOrder user;
    private String productId;
}
