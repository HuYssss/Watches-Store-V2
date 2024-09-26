package com.example.WatchesStoreV2.service.authService.impl;

import com.example.WatchesStoreV2.config.jwtConfig.JwtTokenGenerator;
import com.example.WatchesStoreV2.dto.auth.request.RegisterDto;
import com.example.WatchesStoreV2.dto.auth.response.AuthResponseDto;
import com.example.WatchesStoreV2.entity.RefreshTokenEntity;
import com.example.WatchesStoreV2.entity.User;
import com.example.WatchesStoreV2.helper.TokenGenerator;
import com.example.WatchesStoreV2.helper.TokenType;
import com.example.WatchesStoreV2.mapper.UserMapper;
import com.example.WatchesStoreV2.repository.RefreshTokenRepository;
import com.example.WatchesStoreV2.repository.UserRepository;
import com.example.WatchesStoreV2.service.authService.AuthService;
import com.example.WatchesStoreV2.service.component.MailService;
import com.example.WatchesStoreV2.service.component.UserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userComponentService;
    private final MailService mailService;

    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static final String FACEBOOK_TOKEN_INFO_URL = "https://graph.facebook.com/me";

    @Override
    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            var user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

//            saveUserRefreshToken(user, refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", user.getUsername());

            return UserMapper.mapAuthResponse(user, accessToken);


        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    @Override
    public AuthResponseDto getJWTTokenFromAuthenticationPrincipal(OAuth2User oAuth2User) {
        String email = oAuth2User.getName().toString();

        var user = userRepository.findByUsername(oAuth2User.getName())
                .orElseThrow(() -> {
                    log.error("[AuthService:userSignInAuth] User :{} not found", oAuth2User.getName());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                });

        String accessToken = jwtTokenGenerator.generateAccessToken((Authentication) oAuth2User);
        String refreshToken = jwtTokenGenerator.generateRefreshToken((Authentication) oAuth2User);

        saveUserRefreshToken(user, refreshToken);

        log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", user.getUsername());

        return new AuthResponseDto();
    }

    @Override
    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        User user = refreshTokenEntity.getUser();

        //Now create the Authentication object
        Authentication authentication = createAuthenticationObject(user);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return UserMapper.mapAuthResponse(user, accessToken);
    }

    @Override
    public User register(RegisterDto registerDto) {
        try {
            User user = UserMapper.registerUser(registerDto);
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));

            String token = TokenGenerator.generateVerifyNumber();
            user.setToken(token);
            user.setTokenExpiryDate(Date.from(Instant.now().plusSeconds(15 * 60)));

            this.userComponentService.saveUser(user);
            this.mailService.welcome(registerDto.getEmail(), user.getUsername(), token);

            return user;
        } catch (MongoException e) {
            return null;
        }
    }

    @Override
    public void cleanRefreshTokenRevoked() {
        log.info("[AuthService:cleanRefreshTokenRevoked] Clean refresh token revoked started");
        try {
            List<RefreshTokenEntity> refreshTokens = refreshTokenRepository.findAllByRevoked(true);
            if (!refreshTokens.isEmpty()) {
                refreshTokenRepository.deleteAll(refreshTokens);
                log.info("[AuthService:cleanRefreshTokenRevoked] Clean refresh token revoked successful");
            }
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't clean refresh token revoked");
        }
    }

    @Override
    public String forgotPassword(String email) {
        try {
            Optional<User> user = this.userRepository.findByEmail(email);
            String resetToken = TokenGenerator.generateRandomString();
            if (user.isPresent()) {
                this.mailService.sendResetToken(email, resetToken, user.get().getUsername());

                user.get().setToken(resetToken);
                user.get().setTokenExpiryDate(Date.from(Instant.now().plusSeconds(60 * 60)));

                this.userRepository.save(user.get());
                return "success";
            } else
                return "user not found";
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't generate token reset password");
        }
    }

    @Override
    public User resetPassword(String token, String password) {

        try {
            User user = this.userRepository.findByToken(token).orElse(null);

            if (user != null && user.getTokenExpiryDate().after(new Date())) {

                user.setPassword(this.passwordEncoder.encode(password));
                user.setToken(null);
                user.setTokenExpiryDate(null);

                this.userRepository.save(user);
                this.mailService.resetTokenSuccess(user.getEmail(), user.getUsername());

                return user;
            } else
                return null;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't reset password");
        }
    }

    @Override
    public User verifyUser(String token) {
        try {
            User user = this.userRepository.findByToken(token).orElse(null);

            if (user != null && user.getTokenExpiryDate().after(new Date())) {
                user.setToken(null);
                user.setTokenExpiryDate(null);
                user.setVerified(true);
            }

            this.userRepository.save(user);
            return user;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't verify user");
        }
    }

    @Override
    public User requestVerifyEmail(String email) {
        try {
            User user = this.userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                String token = TokenGenerator.generateVerifyNumber();
                user.setToken(token);
                user.setTokenExpiryDate(Date.from(Instant.now().plusSeconds(15 * 60)));

                this.userRepository.save(user);
                this.mailService.resetTokenSuccess(user.getEmail(), user.getUsername());
            }

            return user;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't request verify email");
        }
    }

    @Override
    public User checkPassword(ObjectId userId, String password) {
        try {
            User user = this.userRepository.findById(userId).orElse(null);
            if (user != null && this.passwordEncoder.matches(password, user.getPassword())) {

                user.setToken(TokenGenerator.generateRandomString());
                user.setTokenExpiryDate(Date.from(Instant.now().plusSeconds(5 * 60)));

                this.userRepository.save(user);

                return user;
            }
            else
                return null;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't check password");

        }
    }

    @Override
    public User updatePassword(ObjectId userId, String password) {
        try {
            User user = this.userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.setPassword(this.passwordEncoder.encode(password));
                this.userRepository.save(user);
                return user;
            }
            else
                return null;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update password");
        }
    }

    @Override
    public AuthResponseDto loginGoogleAuth(String token) {
        log.info("Token Google: {}", token);
        try {
            // Lấy thông tin người dùng từ Google
            JsonObject userInfo = getUserInfoFromGoogle(token);
            String email = userInfo.get("email").getAsString();
            String name = userInfo.get("name").getAsString();
            String avatar = userInfo.has("picture") ? userInfo.get("picture").getAsString() : null;

            // Tìm hoặc tạo người dùng
            User user = findOrCreateUser(email, name, avatar);

            return createAuthResponse(user);

        } catch (Exception e) {
            log.error("Error while processing Google auth: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public AuthResponseDto loginFacebookAuth(String token) {
        try {
            // Lấy thông tin người dùng từ Facebook
            JsonObject userInfo = getUserInfoFromFacebook(token);
            String email = userInfo.has("email") ? userInfo.get("email").getAsString() : null;
            String name = userInfo.has("name") ? userInfo.get("name").getAsString() : null;
            String avatar = userInfo.has("picture") ? userInfo.get("picture").getAsJsonObject().get("data").getAsJsonObject().get("url").getAsString() : null;

            // Tìm hoặc tạo người dùng
            User user = findOrCreateUser(email, name, avatar);

            // Tạo token và trả về thông tin
            return createAuthResponse(user);

        } catch (Exception e) {
            log.error("Error while processing Facebook auth: {}", e.getMessage());
            return null;
        }
    }

    private void saveUserRefreshToken(User user, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private static Authentication createAuthenticationObject(User userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getUsername();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    private JsonObject getUserInfoFromGoogle(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_TOKEN_INFO_URL,
                HttpMethod.GET,
                entity,
                String.class
        );

        return JsonParser.parseString(response.getBody()).getAsJsonObject();
    }

    private JsonObject getUserInfoFromFacebook(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(FACEBOOK_TOKEN_INFO_URL)
                .queryParam("access_token", token)
                .queryParam("fields", "id,first_name,last_name,middle_name,name,name_format,picture,short_name,email,gender")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return JsonParser.parseString(response.getBody()).getAsJsonObject();
    }

    private User findOrCreateUser(String email, String name, String avatar) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setAvatarImg(avatar);
            newUser.setRoles("ROLE_USER");
            newUser.setPassword(passwordEncoder.encode("password"));
            userComponentService.saveUser(newUser);
            return newUser;
        });

        if (optionalUser.isEmpty()) {
            user.setAvatarImg(avatar);
            user.setUsername(name);
            userComponentService.saveUser(user);
        }

        return user;
    }

    private AuthResponseDto createAuthResponse(User user) {
        String accessToken = jwtTokenGenerator.generateAccessTokenGoogleAndFacebook(user);
        String refreshToken = jwtTokenGenerator.generateRefreshTokenGoogleAndFaceBook(user);

        return UserMapper.mapAuthResponse(user, accessToken);
    }
}
