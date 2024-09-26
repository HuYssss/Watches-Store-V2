package com.example.WatchesStoreV2.controller;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.dto.auth.request.RequestAuthCode;
import com.example.WatchesStoreV2.dto.auth.request.RequestAuthEmail;
import com.example.WatchesStoreV2.dto.auth.request.RegisterDto;
import com.example.WatchesStoreV2.dto.auth.request.ResetPassword;
import com.example.WatchesStoreV2.entity.User;
import com.example.WatchesStoreV2.service.authService.AuthService;
import com.example.WatchesStoreV2.service.component.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping()
public class AuthController extends ControllerBase {

    private final AuthService authService;
    private final UserService userComponentService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(Authentication authentication,HttpServletResponse response) {
        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

        String checkUser = this.userComponentService.checkUser(registerDto);

        if (checkUser.equals("email"))
            return response("Email already registered", HttpStatus.BAD_REQUEST);
        else if (checkUser.equals("username"))
            return response("Duplicated username", HttpStatus.BAD_REQUEST);

        User user = this.authService.register(registerDto);

        if (user != null)
            return response(user, HttpStatus.OK);
        else
            return response("Error in processing", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody RequestAuthEmail forgotPassword) {
        String process = this.authService.forgotPassword(forgotPassword.getEmail());

        if (process.equals("success"))
            return response("The password reset email has been sent.", HttpStatus.OK);
        else if (process.equals("user not found"))
            return response("User not found", HttpStatus.NOT_FOUND);
        else
            return response("Error in forgotPassword", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword resetPassword) {
        return response(this.authService.resetPassword(resetPassword.getCode(), resetPassword.getNewPassword()), HttpStatus.OK);
    }

    @GetMapping("/login/success")
    public ResponseEntity<?> login(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return response(this.authService.getJWTTokenFromAuthenticationPrincipal(oAuth2User), HttpStatus.OK);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestParam String token) {
        return response(this.authService.loginGoogleAuth(token), HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<?> loginFacebook(@RequestParam String token) {
        return response(this.authService.loginFacebookAuth(token), HttpStatus.OK);
    }

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@RequestBody RequestAuthCode codeReq) {
        return response(this.authService.verifyUser(codeReq.getCode()), HttpStatus.OK);
    }

    @PostMapping("/request-verify-email")
    public ResponseEntity<?> requestVerifyEmail(@RequestBody RequestAuthEmail requestAuthEmail) {
        return response(this.authService.requestVerifyEmail(requestAuthEmail.getEmail()), HttpStatus.OK);
    }
}
