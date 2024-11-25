package hcmute.edu.vn.watches_store_v2.entity;

import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Review")
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private ObjectId id;
    private int rating;
    private String reviewText;
    private List<String> reviewImages;
    private Date createdAt;
    private ProfileOrder user;
    private ObjectId productId;
}
