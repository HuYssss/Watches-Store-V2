package com.example.WatchesStoreV2.service.authService;

import com.example.WatchesStoreV2.dto.auth.request.RegisterDto;
import com.example.WatchesStoreV2.dto.auth.request.ResetPassword;
import com.example.WatchesStoreV2.dto.auth.response.AuthResponseDto;
import com.example.WatchesStoreV2.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response);
    AuthResponseDto getJWTTokenFromAuthenticationPrincipal(OAuth2User oAuth2User);
    Object getAccessTokenUsingRefreshToken(String authorizationHeader);
    User register(RegisterDto registerDto);
    void cleanRefreshTokenRevoked();
    String forgotPassword(String email);
    User resetPassword(String token, String password);
    User verifyUser(String token);
    User requestVerifyEmail(String email);
    User checkPassword(ObjectId userId, String password);
    User updatePassword(ObjectId userId, String password);

    AuthResponseDto loginGoogleAuth(String token);
    AuthResponseDto loginFacebookAuth(String token);
}
