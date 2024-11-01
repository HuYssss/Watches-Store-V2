package hcmute.edu.vn.watches_store_v2.dto.orderLine.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineUpdateRequest {
    private ObjectId itemId;
    private int quantity;
}
