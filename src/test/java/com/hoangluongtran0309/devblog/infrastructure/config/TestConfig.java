package com.hoangluongtran0309.devblog.infrastructure.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hoangluongtran0309.devblog.infrastructure.security.SecurityConfiguration;
import com.hoangluongtran0309.devblog.infrastructure.security.StubApplicationUserDetailsService;

@TestConfiguration
@Import(SecurityConfiguration.class)
public class TestConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new StubApplicationUserDetailsService(passwordEncoder);
    }
}