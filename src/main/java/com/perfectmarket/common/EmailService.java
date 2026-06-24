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

    public void sendPasswordResetEmail(String toEmail, String code) {
        log.info("=== PASSWORD RESET CODE ===");
        log.info("To: {}", toEmail);
        log.info("Code: {}", code);
        log.info("===========================");

        if (mailPassword == null || mailPassword.isBlank()) {
            log.info("MAIL_PASSWORD not set — dùng code ở trên để test.");
            return;
        }

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject("Perfect Market — Reset your password");
            msg.setText("""
                Hi,

                You requested a password reset. Your password reset verification code is:

                %s

                This code is valid for 30 minutes. If you didn't request this, ignore this email.

                — Perfect Market Team
                """.formatted(code));
            mailSender.send(msg);
            log.info("Reset email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send reset email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendVerificationEmail(String toEmail, String code) {
        log.info("=== EMAIL VERIFICATION CODE ===");
        log.info("To: {}", toEmail);
        log.info("Code: {}", code);
        log.info("===============================");

        if (mailPassword == null || mailPassword.isBlank()) {
            log.info("MAIL_PASSWORD not set — dùng code ở trên để test.");
            return;
        }

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject("Perfect Market — Verify your email");
            msg.setText("""
                Hi,

                Welcome to Perfect Market! Your account verification code is:

                %s

                Please enter this code on the website to activate your account.

                If you didn't register on our website, ignore this email.

                — Perfect Market Team
                """.formatted(code));
            mailSender.send(msg);
            log.info("Verification email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send verification email to {}: {}", toEmail, e.getMessage());
        }
    }
}
