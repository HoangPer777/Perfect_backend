package com.perfectmarket.modules.auth;

import java.util.UUID;
import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Builder;

@Builder
public record UserPrincipal(
        UUID id,
        String email,
        String username,
        String password,
        String status,
        boolean isVerified,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    @Override
    public String getUsername() {
        // This is the method Spring Security calls to get the "principal name".
        // It must not be null.
        return Objects.requireNonNullElse(username, email);
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public boolean isAccountNonLocked() {
        return !"BANNED".equals(status);
    }

    @Override
    public boolean isEnabled() {
        return isVerified;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }
}
