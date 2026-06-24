package com.perfectmarket.modules.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfectmarket.modules.auth.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setCustomArgumentResolvers(new org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver())
                .build();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testLogin_Success() throws Exception {
        LoginRequest req = new LoginRequest("test@example.com", "password");
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                UUID.randomUUID(), "test@example.com", "Test User", "test_user", "avatar.jpg", Set.of("ROLE_CUSTOMER"),
                "Ho Chi Minh City", "Thu Duc District", true, false
        );
        AuthResponse resp = new AuthResponse("mocked_jwt_token", "Bearer", userInfo);

        when(authService.login(any(LoginRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mocked_jwt_token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
        
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    public void testRegister_Success() throws Exception {
        RegisterRequest req = new RegisterRequest("Test User", "test@example.com", "password", "CUSTOMER");
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                UUID.randomUUID(), "test@example.com", "Test User", "test_user", "avatar.jpg", Set.of("ROLE_CUSTOMER"),
                "Ho Chi Minh City", "Thu Duc District", true, false
        );
        AuthResponse resp = new AuthResponse("mocked_jwt_token", "Bearer", userInfo);

        when(authService.register(any(RegisterRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mocked_jwt_token"))
                .andExpect(jsonPath("$.user.fullName").value("Test User"));
        
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    public void testMe_Unauthorized_WhenPrincipalNull() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testMe_Success_WhenPrincipalExists() throws Exception {
        UUID id = UUID.randomUUID();
        UserPrincipal principal = UserPrincipal.builder()
                .id(id)
                .email("test@example.com")
                .username("test@example.com")
                .status("ACTIVE")
                .isVerified(true)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .build();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                id, "test@example.com", "Test User", "test@example.com", "avatar.jpg", Set.of("ROLE_CUSTOMER"),
                "Ho Chi Minh City", "Thu Duc District", true, false
        );
        when(authService.me("test@example.com")).thenReturn(userInfo);

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.fullName").value("Test User"));
    }
}
