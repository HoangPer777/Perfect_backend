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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
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

        // Generate email verification OTP
        String otp = String.format("%06d", new java.util.Random().nextInt(1000000));
        EmailVerificationToken evt = EmailVerificationToken.builder()
                .user(user)
                .token(otp)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();
        emailVerificationTokenRepository.save(evt);

        emailService.sendVerificationEmail(user.getEmail(), otp);

        return AuthResponse.of("", user);
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

        if (!user.isVerified()) {
            throw new IllegalStateException("Account is not verified. Please check your email to verify your account.");
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

            String otp = String.format("%06d", new java.util.Random().nextInt(1000000));
            PasswordResetToken prt = PasswordResetToken.builder()
                    .user(user)
                    .token(otp)
                    .expiresAt(Instant.now().plus(resetExpiryMinutes, ChronoUnit.MINUTES))
                    .build();
            resetTokenRepository.save(prt);
            emailService.sendPasswordResetEmail(email, otp);
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

    // ─── Email Verification ───────────────────────────────────────────────────

    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken evt = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));

        if (evt.isUsed() || evt.isExpired()) {
            throw new IllegalArgumentException("Verification token has expired or already been used");
        }

        User user = evt.getUser();
        user.setVerified(true);
        user.setStatus("ACTIVE");
        userRepository.save(user);

        evt.setUsed(true);
        emailVerificationTokenRepository.save(evt);
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        emailVerificationTokenRepository.deleteByUser(user);

        String otp = String.format("%06d", new java.util.Random().nextInt(1000000));
        EmailVerificationToken evt = EmailVerificationToken.builder()
                .user(user)
                .token(otp)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();
        emailVerificationTokenRepository.save(evt);

        emailService.sendVerificationEmail(user.getEmail(), otp);
    }

    // ─── Update Profile ───────────────────────────────────────────────────────

    @Transactional
    public AuthResponse.UserInfo updateProfile(String email, UpdateProfileRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (req.username() != null && !req.username().equals(user.getUsername())) {
            if (userRepository.findByUsername(req.username()).isPresent()) {
                throw new IllegalArgumentException("Username already in use");
            }
            user.setUsername(req.username());
        }

        user.setFullName(req.fullName());
        if (req.avatarUrl() != null && !req.avatarUrl().isBlank()) {
            user.setAvatarUrl(req.avatarUrl());
        }
        user.setCity(req.city());
        user.setDetailedAddress(req.detailedAddress());
        user.setEmailNotifications(req.emailNotifications());
        user.setPromotionalOffers(req.promotionalOffers());

        userRepository.save(user);

        var roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());

        return new AuthResponse.UserInfo(
                user.getId(), user.getEmail(), user.getFullName(),
                user.getUsername(), user.getAvatarUrl(), roles,
                user.getCity(), user.getDetailedAddress(),
                user.isEmailNotifications(), user.isPromotionalOffers(),
                user.getProvider(),
                user.getSpecialization(),
                user.getBio(),
                user.getPortfolioUrl(),
                user.getSkills(),
                user.getExperienceYears()
        );
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
                user.getUsername(), user.getAvatarUrl(), roles,
                user.getCity(), user.getDetailedAddress(),
                user.isEmailNotifications(), user.isPromotionalOffers(),
                user.getProvider(),
                user.getSpecialization(),
                user.getBio(),
                user.getPortfolioUrl(),
                user.getSkills(),
                user.getExperienceYears()
        );
    }

    // ─── Change Password & Upgrade Role ───────────────────────────────────────

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!"LOCAL".equals(user.getProvider())) {
            throw new IllegalArgumentException("Social login accounts cannot change password.");
        }

        if (user.getPasswordHash() == null ||
            !passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void upgradeToDesigner(
            String email,
            String specialization,
            String bio,
            String portfolioUrl,
            String skills,
            Integer experienceYears
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Role designerRole = roleRepository.findByName("ROLE_DESIGNER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_DESIGNER not found"));

        user.getRoles().clear();
        user.getRoles().add(designerRole);
        user.setSpecialization(specialization);
        user.setBio(bio);
        user.setPortfolioUrl(portfolioUrl);
        user.setSkills(skills);
        user.setExperienceYears(experienceYears != null ? experienceYears : 0);
        userRepository.save(user);
    }

    public UserInfoResponse getDesignerInfo(UUID designerId) {
        User user = userRepository.findById(designerId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + designerId));

        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .city(user.getCity())
                .detailedAddress(user.getDetailedAddress())
                .specialization(user.getSpecialization())
                .bio(user.getBio())
                .portfolioUrl(user.getPortfolioUrl())
                .skills(user.getSkills())
                .experienceYears(user.getExperienceYears())
                .status(user.getStatus())
                .isVerified(user.isVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
