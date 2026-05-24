package com.perfectmarket.config;

import com.perfectmarket.modules.auth.Role;
import com.perfectmarket.modules.auth.RoleRepository;
import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();
        String provider = oauthToken.getAuthorizedClientRegistrationId().toUpperCase(); // GOOGLE | FACEBOOK

        String email = oauthUser.getAttribute("email");
        if (email == null) {
            response.sendRedirect(frontendUrl + "/login?error=oauth_no_email");
            return;
        }
        String name  = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");
        String providerId = oauthUser.getName();

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
            return userRepository.save(User.builder()
                .email(email)
                .fullName(name)
                .avatarUrl(picture)
                .provider(provider)
                .providerId(providerId)
                .isVerified(true)
                .roles(Set.of(customerRole))
                .build());
        });

        String token = jwtUtil.generateToken(user.getEmail());
        // Redirect về frontend kèm token
        response.sendRedirect(frontendUrl + "/auth/callback?token=" + token);
    }
}
