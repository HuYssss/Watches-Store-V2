package com.example.WatchesStoreV2.config;

import com.example.WatchesStoreV2.dto.auth.response.AuthResponseDto;
import com.example.WatchesStoreV2.entity.User;
import com.example.WatchesStoreV2.mapper.UserMapper;
import com.example.WatchesStoreV2.repository.UserRepository;
import com.example.WatchesStoreV2.service.authService.impl.AuthServiceImpl;
import com.mongodb.MongoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        log.info("OauthAuthenticationSuccessHandler");

        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        log.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            log.info(key + ": " + value);
        });

        User user = UserMapper.mapNewEmptyUser();

        if (authorizedClientRegistrationId.equals("google")) {

            user.setUsername(oauthUser.getAttribute("sub").toString());
            user.setPassword(oauthUser.getAttribute("sub").toString());
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setAvatarImg(oauthUser.getAttribute("picture").toString());
            user.setProvider(authorizedClientRegistrationId);

        } else if (authorizedClientRegistrationId.equals("facebook")) {
            user.setUsername(oauthUser.getAttribute("id").toString());
            user.setPassword(oauthUser.getAttribute("id").toString());
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProvider(authorizedClientRegistrationId);

        } else {
            log.info("OauthAuthenticationSuccessHandler: unknown provider");
        }

        User processedUser = checkAndSaveUser(user);

        new DefaultRedirectStrategy().sendRedirect(request, response, "/login/success");
    }

    public User checkAndSaveUser(User user) {
        try {
            Optional<User> userPresent = this.userRepository.findByEmail(user.getEmail());

            if (userPresent.isPresent()) {
                this.userRepository.save(user);
                return user;
            }

            return userPresent.get();
        } catch (MongoException e) {
            throw new MongoException("Can't save user");
        }
    }

}
