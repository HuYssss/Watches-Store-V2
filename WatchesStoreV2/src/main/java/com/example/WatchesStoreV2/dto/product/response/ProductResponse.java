package com.example.WatchesStoreV2.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

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

    private String thickness;

    private String size;

    private String wireMaterial;

    private String shellMaterial;

    private String style;

    private String feature;

    private String shape;

    private String condition;

    private String weight;

    private String genderUser;

    private String description;

    private String color;

    private double discount;

    private String category;

    private int amount;

    private int waterproof;

    private String state;
}
