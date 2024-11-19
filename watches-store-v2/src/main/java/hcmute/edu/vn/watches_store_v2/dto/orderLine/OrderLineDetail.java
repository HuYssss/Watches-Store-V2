package hcmute.edu.vn.watches_store_v2.dto.orderLine;

import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDetail {
    private String id;

    private ProductOrder product;

    private int quantity;
}
