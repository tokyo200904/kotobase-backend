package kotobase_backend.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class Oauth2loginfailurehandler
        extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.oauth2.authorizedRedirectUris[0]}")
    private String redirectUri;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            message = "Đăng nhập OAuth2 thất bại";
        }
        log.error("Đăng nhập thất bại: {}", message);
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        String targetUrl = redirectUri + "?error=" + encodedMessage;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}