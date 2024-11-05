package hcmute.edu.vn.watches_store_v2.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;

    private String productName;

    private List<String> img;

    private double price;

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

    private List<String> color;

    private double discount;

    private String category;

    private int amount;

    private int waterproof;

    private String state;
}
