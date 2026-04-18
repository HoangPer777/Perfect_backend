package com.perfectmarket.modules.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // TODO: Implement Login (Email/Password)
    @PostMapping("/login")
    public String login() {
        return "TODO: Implement JWT Login";
    }

    // TODO: Implement Registration
    @PostMapping("/register")
    public String register() {
        return "TODO: Implement User Registration";
    }

    // TODO: Implement Social Login (OAuth2 handled by Spring Security)
    
    // TODO: Implement Forgot Password / Reset Password
}
