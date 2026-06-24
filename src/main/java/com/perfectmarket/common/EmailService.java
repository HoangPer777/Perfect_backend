package com.perfectmarket.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        // Always log for dev/testing — copy link từ console backend
        log.info("=== PASSWORD RESET LINK ===");
        log.info("To: {}", toEmail);
        log.info("Link: {}", resetLink);
        log.info("===========================");

        // Chỉ gửi email thật nếu MAIL_PASSWORD đã được cấu hình
        if (mailPassword == null || mailPassword.isBlank()) {
            log.info("MAIL_PASSWORD not set — dùng link ở trên để test.");
            return;
        }

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject("Perfect Market — Reset your password");
            msg.setText("""
                Hi,

                You requested a password reset. Click the link below (valid 30 minutes):

                %s

                If you didn't request this, ignore this email.

                — Perfect Market Team
                """.formatted(resetLink));
            mailSender.send(msg);
            log.info("Reset email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send reset email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String verifyLink = frontendUrl + "/verify-email?token=" + token;

        log.info("=== EMAIL VERIFICATION LINK ===");
        log.info("To: {}", toEmail);
        log.info("Link: {}", verifyLink);
        log.info("===============================");

        if (mailPassword == null || mailPassword.isBlank()) {
            log.info("MAIL_PASSWORD not set — dùng link ở trên để test.");
            return;
        }

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject("Perfect Market — Verify your email");
            msg.setText("""
                Hi,

                Welcome to Perfect Market! Please verify your email by clicking the link below:

                %s

                If you didn't register on our website, ignore this email.

                — Perfect Market Team
                """.formatted(verifyLink));
            mailSender.send(msg);
            log.info("Verification email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send verification email to {}: {}", toEmail, e.getMessage());
        }
    }
}
