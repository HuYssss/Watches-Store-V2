package com.example.WatchesStoreV2.dto.user.response;

import com.example.WatchesStoreV2.dto.address.response.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
