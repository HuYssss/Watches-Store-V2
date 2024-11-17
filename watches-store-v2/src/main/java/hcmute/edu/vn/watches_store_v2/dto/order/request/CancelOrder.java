package hcmute.edu.vn.watches_store_v2.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrder {
    private ObjectId id;
    private String message;
}
