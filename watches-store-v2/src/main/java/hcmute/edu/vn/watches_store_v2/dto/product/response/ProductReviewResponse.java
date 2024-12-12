package hcmute.edu.vn.watches_store_v2.dto.product.response;

import hcmute.edu.vn.watches_store_v2.dto.product.Option;
import hcmute.edu.vn.watches_store_v2.dto.review.response.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewResponse {
    private String id;

    private String productName;

    private List<String> img;

    private String brand;

    private String origin;

    private String wireMaterial;

    private String shellMaterial;

    private String style;

    private String feature;

    private String shape;

    private double weight;

    private double length;

    private double width;

    private double height;

    private String genderUser;

    private String description;

    private List<Option> option;

    private String category;

    private int waterproof;

    private String type;

    private String stateProduct;

    private int access;

    private List<ReviewResponse> reviews;
}
