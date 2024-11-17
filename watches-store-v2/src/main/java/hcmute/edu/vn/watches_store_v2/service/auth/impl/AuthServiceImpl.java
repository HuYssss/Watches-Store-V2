package hcmute.edu.vn.watches_store_v2.service.auth.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.config.jwt.JwtTokenGenerator;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.dto.auth.response.AuthResponseDto;
import hcmute.edu.vn.watches_store_v2.entity.RefreshToken;
import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.helper.TokenGenerator;
import hcmute.edu.vn.watches_store_v2.helper.TokenType;
import hcmute.edu.vn.watches_store_v2.mapper.UserMapper;
import hcmute.edu.vn.watches_store_v2.repository.RefreshTokenRepository;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
import hcmute.edu.vn.watches_store_v2.service.auth.AuthService;
import hcmute.edu.vn.watches_store_v2.service.component.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static final String FACEBOOK_TOKEN_INFO_URL = "https://graph.facebook.com/me";

    @Override
    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication) {
        try
        {
            var user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            saveUserRefreshToken(user,refreshToken);
            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", user.getUsername());

            return UserMapper.mapAuthResponse(user, accessToken, refreshToken);

        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }

    @Override
    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        User user = this.userRepository.findById(refreshTokenEntity.getUser()).orElse(null);


        if (user != null) {
            Authentication authentication =  createAuthenticationObject(user);

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

            return  UserMapper.mapAuthResponse(user, accessToken, null);
        }
        else
            return null;
    }

    @Override
    public User register(RegisterRequest register) {
        try {
            User user = UserMapper.mapUser(register);
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));

            String token = TokenGenerator.generateVerifyNumber();
            user.setToken(token);
            user.setTokenExpiryDate(Date.from(Instant.now().plusSeconds(15 * 60)));

            this.userRepository.save(user);
            this.mailService.welcome(register.getEmail(), user.getUsername(), token);

            return user;
        } catch (MongoException e) {
            return null;
        }
    }

    @Override
    public AuthResponseDto loginGoogle(String token) {
        log.info("Token Google: {}", token);
        try {
            JsonObject userInfo = getUserInfoFromGoogle(token);
            String email = userInfo.get("email").getAsString();
            String name = userInfo.get("name").getAsString();
            String avatar = userInfo.has("picture") ? userInfo.get("picture").getAsString() : null;

            User user = findOrCreateUser(email, name, avatar);

            Authentication authentication = createAuthenticationObject(user);

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            saveUserRefreshToken(user,refreshToken);

            log.info("[AuthService:userSignInGoogle] Access token for user:{}, has been generated", user.getUsername());
            return UserMapper.mapAuthResponse(user, accessToken, refreshToken);

        } catch (Exception e) {
            log.error("[AuthService:userSignInGoogle] Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }

    @Override
    public AuthResponseDto loginFacebook(String token) {
        log.info("Token Facebook: {}", token);
        try {
            JsonObject userInfo = getUserInfoFromFacebook(token);
            String email = userInfo.has("email") ? userInfo.get("email").getAsString() : null;
            String name = userInfo.has("name") ? userInfo.get("name").getAsString() : null;
            String avatar = userInfo.has("picture") ? userInfo.get("picture").getAsJsonObject().get("data").getAsJsonObject().get("url").getAsString() : null;

            User user = findOrCreateUser(email, name, avatar);

            Authentication authentication = createAuthenticationObject(user);

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            saveUserRefreshToken(user,refreshToken);

            log.info("[AuthService:userSignInFacebook] Access token for user:{}, has been generated", user.getUsername());
            return UserMapper.mapAuthResponse(user, accessToken, refreshToken);
        } catch (Exception e) {
            log.error("[AuthService:userSignInFacebook] Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
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

    private static Authentication createAuthenticationObject(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String roles = user.getRoles();

        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    private void saveUserRefreshToken(User user, String refreshToken) {
        var refreshTokenEntity = RefreshToken.builder()
                .user(user.getId())
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        this.refreshTokenRepository.save(refreshTokenEntity);
    }

    private JsonObject getUserInfoFromGoogle(String token) {
        log.info("Jwt token: {}", token);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info(entity.toString());

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

        log.info("[AuthService:getUserInfoFromFacebook] Call facebook uri: {}", url);
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
            this.userRepository.save(newUser);
            return newUser;
        });

        if (optionalUser.isEmpty()) {
            user.setAvatarImg(avatar);
            user.setUsername(name);
            this.userRepository.save(user);
        }

        return user;
    }
}
