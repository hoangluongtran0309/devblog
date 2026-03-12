package com.hoangluongtran0309.devblog.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

        private final PasswordEncoder passwordEncoder;
        private final UserDetailsService userDetailsService;

        public SecurityConfiguration(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
                this.passwordEncoder = passwordEncoder;
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(
                                userDetailsService);
                daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

                return daoAuthenticationProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        @Value("${devblog.remember-me-key}") String rememberMeKey) throws Exception {

                http
                                .csrf(csrf -> Customizer.withDefaults())
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                                .permitAll()
                                                .requestMatchers("/svg/*").permitAll()
                                                .requestMatchers("/admin/login").permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .anyRequest().permitAll())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/admin/login")
                                                .loginProcessingUrl("/admin/login")
                                                .defaultSuccessUrl("/admin/dashboard", true)
                                                .failureUrl("/admin/login?error")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/admin/logout")
                                                .logoutSuccessUrl("/admin/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .rememberMe(rememberMe -> rememberMe
                                                .key(rememberMeKey)
                                                .rememberMeParameter("remember-me")
                                                .tokenValiditySeconds(7 * 24 * 60 * 60)
                                                .userDetailsService(userDetailsService))
                                .httpBasic(httpBasic -> httpBasic.disable());

                return http.build();
        }
}
