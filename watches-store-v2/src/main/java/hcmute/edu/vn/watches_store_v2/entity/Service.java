package hcmute.edu.vn.watches_store_v2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Service")
public class Service {
    private ObjectId id;
    private String name;
    private String phone;
    private String email;
    private String message;
    private String type;
    private List<String> img;
    private String state;
}
