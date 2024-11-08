package hcmute.edu.vn.watches_store_v2.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {
    private List<ProductResponse> productResponses;
    private int totalPages;
    private int totalProducts;
    private List<String> wireMaterial = new ArrayList<>();
    private List<String> shape = new ArrayList<>();
    private List<Integer> waterProof = new ArrayList<>();

    public void addTypeWireMaterial(String wireMaterial) {
        this.wireMaterial.add(wireMaterial);
    }

    public void addTypeShape(String shape) {
        this.shape.add(shape);
    }

    public void addTypeWaterProof(int waterProof) {
        this.waterProof.add(waterProof);
    }
}
