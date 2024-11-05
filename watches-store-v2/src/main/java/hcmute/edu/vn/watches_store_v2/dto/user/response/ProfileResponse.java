package hcmute.edu.vn.watches_store_v2.dto.user.response;

import hcmute.edu.vn.watches_store_v2.dto.user.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String id;

    private String email;

    private String phone;

    private String username;

    private String fullName;

    private String avatarImg;

    private Address address;

    private String reasonBlock;

    private Date blockAt;

    private String state;

    private boolean verified;

    private boolean isAdmin;
}
