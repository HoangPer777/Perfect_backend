package com.perfectmarket.config;

import com.perfectmarket.modules.auth.UserPrincipal;
import com.perfectmarket.modules.auth.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.isValid(token)) {
                String email = jwtUtil.extractEmail(token);

                userRepository.findByEmail(email).ifPresent(user -> {
                    var authorities = user.getRoles().stream()
                            .map(r -> new SimpleGrantedAuthority(r.getName()))
                            .collect(Collectors.toList());

                    UserPrincipal principal = UserPrincipal.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .username(user.getUsername() != null ? user.getUsername() : user.getEmail())
                            .status(user.getStatus() != null ? user.getStatus() : "ACTIVE")
                            .isVerified(user.isVerified())
                            .authorities(authorities)
                            .build();

                    var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    log.debug("DEBUG: Auth được set: {}", auth.getPrincipal());
                });
            }
        }
        chain.doFilter(req, res);
    }
}
