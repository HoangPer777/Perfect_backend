package com.perfectmarket.modules.auth.security;

import com.perfectmarket.modules.auth.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtCurrentUserProvider implements CurrentUserProvider {
    @Override
    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        if (auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.id();
        }
        return null;
    }
}
