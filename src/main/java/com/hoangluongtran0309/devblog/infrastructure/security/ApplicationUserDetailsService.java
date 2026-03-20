package com.hoangluongtran0309.devblog.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.admin.Email;
import com.hoangluongtran0309.devblog.admin.User;
import com.hoangluongtran0309.devblog.admin.UserRepository;

@Service
@Transactional(readOnly = true)
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(new Email(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new ApplicationUserDetails(user);
    }
}
