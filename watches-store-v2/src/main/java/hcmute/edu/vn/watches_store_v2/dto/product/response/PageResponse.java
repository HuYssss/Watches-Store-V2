package hcmute.edu.vn.watches_store_v2.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {
    private List<ProductResponse> productResponses;
    private int totalPages;
    private int totalProducts;
}
