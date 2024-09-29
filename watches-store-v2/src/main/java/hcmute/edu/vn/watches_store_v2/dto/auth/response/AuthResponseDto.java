package hcmute.edu.vn.watches_store_v2.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.edu.vn.watches_store_v2.helper.TokenType;
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

    @JsonProperty("refresh_token")
    private String refresh_token;

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