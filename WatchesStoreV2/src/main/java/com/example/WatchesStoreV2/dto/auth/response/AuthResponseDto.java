package com.example.WatchesStoreV2.dto.auth.response;

import com.example.WatchesStoreV2.dto.user.response.ProfileResponse;
import com.example.WatchesStoreV2.helper.TokenType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    @JsonProperty("access_token")
    private String accessToken;

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
