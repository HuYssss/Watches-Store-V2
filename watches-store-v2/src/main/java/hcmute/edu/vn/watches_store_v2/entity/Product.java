package hcmute.edu.vn.watches_store_v2.entity;

import hcmute.edu.vn.watches_store_v2.dto.product.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private ObjectId id;

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

    private ObjectId category;

    private int waterproof;

    private String type;

    private String state;
}
