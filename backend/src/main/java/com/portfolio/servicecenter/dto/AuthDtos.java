package com.portfolio.servicecenter.dto;

import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {
    private AuthDtos() {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record LoginResponse(
            String accessToken,
            String tokenType,
            String role
    ) {}

    public record MeResponse(
            String username,
            String role
    ) {}
}
