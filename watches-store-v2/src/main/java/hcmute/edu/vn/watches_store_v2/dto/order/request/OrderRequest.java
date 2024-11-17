package hcmute.edu.vn.watches_store_v2.dto.order.request;

import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<ObjectId> productItem;
    private String paymentMethod;
    private double shippingPrice;
    private String couponCode;
    private ProfileOrder profile;
}
