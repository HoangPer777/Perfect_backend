package com.perfectmarket.modules.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findByUserEmailAndTokenAndUsedFalse(String email, String token);
    void deleteByUser(User user);
}
