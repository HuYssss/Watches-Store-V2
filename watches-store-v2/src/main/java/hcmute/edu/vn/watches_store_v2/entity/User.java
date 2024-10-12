package hcmute.edu.vn.watches_store_v2.entity;

import hcmute.edu.vn.watches_store_v2.dto.user.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "User")
public class User {
    @Id
    private ObjectId id;

    private String email;

    private String phone;

    private String username;

    private String password;

    private String fullName;

    private String avatarImg;

    private Address address;

    private String roles;

    private String state;

    private String provider;

    private boolean verified;

    private String token;

    private Date tokenExpiryDate;
}
