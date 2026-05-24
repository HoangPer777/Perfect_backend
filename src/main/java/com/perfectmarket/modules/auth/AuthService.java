package com.perfectmarket.modules.auth;

import com.perfectmarket.common.EmailService;
import com.perfectmarket.config.JwtUtil;
import com.perfectmarket.modules.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${app.reset-password-expiry-minutes:30}")
    private int resetExpiryMinutes;

    // ─── Register ────────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        String roleName = req.role().equalsIgnoreCase("DESIGNER")
                ? "ROLE_DESIGNER" : "ROLE_CUSTOMER";

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = User.builder()
                .email(req.email())
                .fullName(req.fullName())
                .username(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .provider("LOCAL")
                .isVerified(false)
                .status("ACTIVE")
                .roles(Set.of(role))
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return AuthResponse.of(token, user);
    }

    // ─── Login ────────────────────────────────────────────────────────────────

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!"LOCAL".equals(user.getProvider())) {
            String provider = user.getProvider();
            throw new IllegalArgumentException("This account uses " + provider + " login. Please sign in with " + provider + ".");
        }

        if (user.getPasswordHash() == null ||
            !passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if ("BANNED".equals(user.getStatus())) {
            throw new IllegalStateException("Account is banned");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return AuthResponse.of(token, user);
    }

    // ─── Forgot Password ─────────────────────────────────────────────────────

    @Transactional
    public void forgotPassword(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            // Invalidate old tokens
            resetTokenRepository.deleteByUser(user);

            String token = UUID.randomUUID().toString();
            PasswordResetToken prt = PasswordResetToken.builder()
                    .user(user)
                    .token(token)
                    .expiresAt(Instant.now().plus(resetExpiryMinutes, ChronoUnit.MINUTES))
                    .build();
            resetTokenRepository.save(prt);
            emailService.sendPasswordResetEmail(email, token);
        });
        // Always return success to prevent email enumeration
    }

    // ─── Reset Password ───────────────────────────────────────────────────────

    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        PasswordResetToken prt = resetTokenRepository.findByToken(req.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        if (prt.isUsed() || prt.isExpired()) {
            throw new IllegalArgumentException("Token has expired or already been used");
        }

        User user = prt.getUser();
        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);

        prt.setUsed(true);
        resetTokenRepository.save(prt);
    }

    // ─── Get current user ─────────────────────────────────────────────────────

    public AuthResponse.UserInfo me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var roles = user.getRoles().stream()
                .map(r -> r.getName())
                .collect(java.util.stream.Collectors.toSet());
        return new AuthResponse.UserInfo(
                user.getId(), user.getEmail(), user.getFullName(),
                user.getUsername(), user.getAvatarUrl(), roles
        );
    }
}
