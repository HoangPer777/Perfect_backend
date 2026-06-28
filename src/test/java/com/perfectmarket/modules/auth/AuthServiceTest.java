package com.perfectmarket.modules.auth;

import com.perfectmarket.common.EmailService;
import com.perfectmarket.config.JwtUtil;
import com.perfectmarket.modules.auth.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordResetTokenRepository resetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private EmailService emailService;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;
    private Role customerRole;

    @BeforeEach
    public void setup() {
        customerRole = new Role(1L, "ROLE_CUSTOMER");
        
        sampleUser = User.builder()
                .email("test@example.com")
                .fullName("Test User")
                .username("test@example.com")
                .passwordHash("hashed_password")
                .provider("LOCAL")
                .isVerified(false)
                .status("ACTIVE")
                .roles(Set.of(customerRole))
                .build();

        ReflectionTestUtils.setField(authService, "resetExpiryMinutes", 30);
    }

    @Test
    public void testRegister_Success() {
        RegisterRequest req = new RegisterRequest("Test User", "test@example.com", "password", "CUSTOMER");
        
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(passwordEncoder.encode("password")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(emailVerificationTokenRepository.save(any(EmailVerificationToken.class))).thenReturn(null);

        AuthResponse response = authService.register(req);

        assertNotNull(response);
        assertEquals("", response.accessToken());
        assertEquals("test@example.com", response.user().email());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailVerificationTokenRepository, times(1)).save(any(EmailVerificationToken.class));
        verify(emailService, times(1)).sendVerificationEmail(eq("test@example.com"), anyString());
    }

    @Test
    public void testRegister_EmailAlreadyInUse() {
        RegisterRequest req = new RegisterRequest("Test User", "test@example.com", "password", "CUSTOMER");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(req);
        });
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLogin_Success() {
        LoginRequest req = new LoginRequest("test@example.com", "password");
        sampleUser.setVerified(true);
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password", "hashed_password")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mocked_jwt_token");

        AuthResponse response = authService.login(req);

        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.accessToken());
        assertEquals("test@example.com", response.user().email());
    }

    @Test
    public void testLogin_InvalidPassword() {
        LoginRequest req = new LoginRequest("test@example.com", "wrong_password");
        sampleUser.setVerified(true);
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrong_password", "hashed_password")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(req);
        });
    }

    @Test
    public void testLogin_SocialAccountAttemptsLocalLogin() {
        LoginRequest req = new LoginRequest("test@example.com", "password");
        sampleUser.setProvider("GOOGLE");
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));

        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(req);
        });
    }

    @Test
    public void testForgotPassword_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        
        authService.forgotPassword("test@example.com");

        verify(resetTokenRepository, times(1)).deleteByUser(sampleUser);
        verify(resetTokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), anyString());
    }

    @Test
    public void testResetPassword_Success() {
        PasswordResetToken token = PasswordResetToken.builder()
                .id(1L)
                .user(sampleUser)
                .token("valid_token")
                .expiresAt(Instant.now().plusSeconds(600))
                .used(false)
                .build();
        
        ResetPasswordRequest req = new ResetPasswordRequest("valid_token", "new_password");
        
        when(resetTokenRepository.findByToken("valid_token")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("new_password")).thenReturn("new_hashed_password");

        authService.resetPassword(req);

        assertEquals("new_hashed_password", sampleUser.getPasswordHash());
        assertTrue(token.isUsed());
        verify(userRepository, times(1)).save(sampleUser);
        verify(resetTokenRepository, times(1)).save(token);
    }
}
