package hcmute.edu.vn.watches_store_v2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "RefreshToken")
public class RefreshToken {
    @Id
    private ObjectId id;

    private String refreshToken;

    private boolean revoked;

    private ObjectId user;
}
