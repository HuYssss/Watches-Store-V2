package hcmute.edu.vn.watches_store_v2.dto.user.response;

import hcmute.edu.vn.watches_store_v2.dto.user.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileOrder {
    private String name;
    private String phone;
    private String email;
    private Address address;
}