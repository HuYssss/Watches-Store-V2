package hcmute.edu.vn.watches_store_v2.dto.orderLine.response;

import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineResponse {

    private String id;

    private ProductResponse product;

    private int quantity;

    private String option;

}
