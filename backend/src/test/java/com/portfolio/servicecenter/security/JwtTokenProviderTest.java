package com.portfolio.servicecenter.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    @Test
    void issuedTokenCanBeValidatedAndRead() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "portfolio-test-secret-key-with-more-than-32-bytes",
                10
        );
        var user = User.withUsername("admin").password("ignored").roles("ADMIN").build();

        String token = provider.createToken(user);

        assertThat(provider.isValid(token)).isTrue();
        assertThat(provider.getUsername(token)).isEqualTo("admin");
    }

    @Test
    void malformedTokenIsRejected() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "portfolio-test-secret-key-with-more-than-32-bytes",
                10
        );

        assertThat(provider.isValid("not-a-jwt")).isFalse();
    }
}
