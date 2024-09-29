package hcmute.edu.vn.watches_store_v2.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String address;

    private String state;

    private boolean verified;

    private boolean isAdmin;
}
