package hcmute.edu.vn.watches_store_v2.dto.product.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiscount {
    private ObjectId productId;
    private double discount;
}