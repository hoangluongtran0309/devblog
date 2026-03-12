package com.hoangluongtran0309.devblog.infrastructure.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hoangluongtran0309.devblog.admin.Email;
import com.hoangluongtran0309.devblog.admin.User;
import com.hoangluongtran0309.devblog.admin.UserId;
import com.hoangluongtran0309.devblog.admin.UserName;

public class StubApplicationUserDetailsService implements UserDetailsService {

    public static final String USERNAME_ADMIN = "admin@test.com";
    private final Map<String, ApplicationUserDetails> users = new HashMap<>();

    public StubApplicationUserDetailsService(PasswordEncoder passwordEncoder) {
        addUser(new ApplicationUserDetails(createAdministrator(passwordEncoder)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(users.get(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private void addUser(ApplicationUserDetails userDetails) {
        users.put(userDetails.getUsername(), userDetails);
    }

    private User createAdministrator(PasswordEncoder passwordEncoder) {
        return User.createAdministrator(UserId.generate(), new UserName("Luong", "Tran"), new Email(USERNAME_ADMIN),
                passwordEncoder.encode("12345678"));
    }
}
