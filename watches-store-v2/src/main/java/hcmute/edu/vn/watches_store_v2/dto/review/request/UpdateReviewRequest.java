package hcmute.edu.vn.watches_store_v2.dto.review.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewRequest {
    private ObjectId id;
    private int rating;
    private String reviewText;
    private List<String> reviewImages;
}
