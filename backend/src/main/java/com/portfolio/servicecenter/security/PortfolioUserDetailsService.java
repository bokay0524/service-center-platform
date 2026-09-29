package com.portfolio.servicecenter.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PortfolioUserDetailsService implements UserDetailsService {
    private final Map<String, UserDetails> users;

    public PortfolioUserDetailsService(PasswordEncoder passwordEncoder) {
        this.users = Map.of(
                "admin", User.withUsername("admin")
                        .password(passwordEncoder.encode("admin1234"))
                        .roles("ADMIN")
                        .build(),
                "center", User.withUsername("center")
                        .password(passwordEncoder.encode("center1234"))
                        .roles("CENTER")
                        .build()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user");
        }
        return user;
    }
}
