package hcmute.edu.vn.watches_store_v2.dto.user.request;

import hcmute.edu.vn.watches_store_v2.dto.user.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String phone;

    private String fullName;

    private String avatarImg;

    private String email;

    private Address address;
}
