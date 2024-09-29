package hcmute.edu.vn.watches_store_v2.service.auth;

import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.dto.auth.response.AuthResponseDto;
import hcmute.edu.vn.watches_store_v2.entity.User;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication);
    Object getAccessTokenUsingRefreshToken(String authorizationHeader);
    User register(RegisterRequest registerDto);

    AuthResponseDto loginGoogle(String token);
    AuthResponseDto loginFacebook(String token);

    String forgotPassword(String email);
    User resetPassword(String token, String password);
    User verifyUser(String token);
    User requestVerifyEmail(String email);
    User checkPassword(ObjectId userId, String password);
}
