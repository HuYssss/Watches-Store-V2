package com.example.WatchesStoreV2.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantProduct {
    private List<String> img;
    private int amount;
    private String color;
}
