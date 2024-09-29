package hcmute.edu.vn.watches_store_v2.service.auth;

import hcmute.edu.vn.watches_store_v2.helper.TokenType;
import hcmute.edu.vn.watches_store_v2.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!authHeader.startsWith(TokenType.Bearer.name())) {
            return;
        }

        final String refreshToken = authHeader.substring(7);

        var storedRefreshToken = this.refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(token -> {
                    token.setRevoked(true);
                    this.refreshTokenRepository.delete(token);
                    return token;
                })
                .orElse(null);
    }
}
