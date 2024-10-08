package hcmute.edu.vn.watches_store_v2.dto.productItem.response;

import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemResponse {

    private String id;

    private ProductResponse product;

    private int quantity;

}
