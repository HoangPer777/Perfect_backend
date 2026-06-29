package com.perfectmarket.modules.auth;

import com.perfectmarket.modules.auth.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
        @Valid @RequestBody ForgotPasswordRequest req
    ) {
        authService.forgotPassword(req.email());
        return ResponseEntity.ok(Map.of("message", "If that email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
        @Valid @RequestBody ResetPasswordRequest req
    ) {
        authService.resetPassword(req);
        return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserInfo> me(
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.me(principal.email()));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(
        @Valid @RequestBody VerifyEmailRequest req
    ) {
        authService.verifyEmail(req.token(), req.email());
        return ResponseEntity.ok(Map.of("message", "Email verified successfully."));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerification(
        @Valid @RequestBody ResendVerificationRequest req
    ) {
        authService.resendVerificationEmail(req.email());
        return ResponseEntity.ok(Map.of("message", "Verification email resent successfully."));
    }

    @PatchMapping("/profile")
    public ResponseEntity<AuthResponse.UserInfo> updateProfile(
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody UpdateProfileRequest req
    ) {
        if (principal == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.updateProfile(principal.email(), req));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody ChangePasswordRequest req
    ) {
        if (principal == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        authService.changePassword(principal.email(), req.oldPassword(), req.newPassword());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
    }

    @PostMapping("/upgrade-designer")
    public ResponseEntity<Map<String, String>> upgradeDesigner(
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody UpgradeDesignerRequest req
    ) {
        if (principal == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        authService.upgradeToDesigner(
            principal.email(),
            req.specialization(),
            req.bio(),
            req.portfolioUrl(),
            req.skills(),
            req.experienceYears()
        );
        return ResponseEntity.ok(Map.of("message", "Upgraded to designer successfully."));
    }

    /** Logout — client chỉ cần xóa token phía frontend */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully."));
    }

    @GetMapping("/profile-designer")
    public ResponseEntity<UserInfoResponse> getProfileDesigner(@RequestParam UUID id) {
        return ResponseEntity.ok(authService.getDesignerInfo(id));
    }
}
