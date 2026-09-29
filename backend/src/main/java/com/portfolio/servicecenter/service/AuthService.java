package com.portfolio.servicecenter.service;

import com.portfolio.servicecenter.dto.AuthDtos.LoginRequest;
import com.portfolio.servicecenter.dto.AuthDtos.LoginResponse;
import com.portfolio.servicecenter.security.JwtTokenProvider;
import com.portfolio.servicecenter.security.PortfolioUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PortfolioUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
                       PortfolioUserDetailsService userDetailsService,
                       JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String role = user.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER")
                .replace("ROLE_", "");
        return new LoginResponse(tokenProvider.createToken(user), "Bearer", role);
    }
}
