package hcmute.edu.vn.watches_store_v2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "OrderLine")
@AllArgsConstructor
@NoArgsConstructor
public class OrderLine {
    @Id
    private ObjectId id;

    private ObjectId product;

    private int quantity;

    private String option;

    private ObjectId user;

    private ObjectId order;
}
