package com.portfolio.servicecenter.controller;

import com.portfolio.servicecenter.dto.AuthDtos.LoginRequest;
import com.portfolio.servicecenter.dto.AuthDtos.LoginResponse;
import com.portfolio.servicecenter.dto.AuthDtos.MeResponse;
import com.portfolio.servicecenter.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER")
                .replace("ROLE_", "");
        return new MeResponse(authentication.getName(), role);
    }
}
