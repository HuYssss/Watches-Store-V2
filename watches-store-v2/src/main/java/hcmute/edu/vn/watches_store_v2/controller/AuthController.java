package hcmute.edu.vn.watches_store_v2.controller;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.RequestAuthCode;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.RequestAuthEmail;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.ResetPassword;
import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.service.auth.AuthService;
import hcmute.edu.vn.watches_store_v2.service.component.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class AuthController extends ControllerBase {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(Authentication authentication){
        return response(authService.getJwtTokensAfterAuthentication(authentication), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerDto) {

        String checkUser = this.userService.checkUser(registerDto);

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

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestParam String token) {
        return response(this.authService.loginGoogle(token), HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<?> loginFacebook(@RequestParam String token) {
        return response(this.authService.loginFacebook(token), HttpStatus.OK);
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

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@RequestBody RequestAuthCode codeReq) {
        return response(this.authService.verifyUser(codeReq.getCode()), HttpStatus.OK);
    }

    @PostMapping("/request-verify-email")
    public ResponseEntity<?> requestVerifyEmail(@RequestBody RequestAuthEmail requestAuthEmail) {
        return response(this.authService.requestVerifyEmail(requestAuthEmail.getEmail()), HttpStatus.OK);
    }
}
