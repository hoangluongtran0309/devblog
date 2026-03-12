package com.hoangluongtran0309.devblog.admin.web;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hoangluongtran0309.devblog.admin.UserService;
import com.hoangluongtran0309.devblog.infrastructure.security.SecurityConfiguration;
import com.hoangluongtran0309.devblog.infrastructure.security.StubApplicationUserDetailsService;

@WebMvcTest(AdminController.class)
@TestPropertySource(properties = "devblog.remember-me-key=test-secret-key")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getDashboardRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void getChangePassword() throws Exception {
        mockMvc.perform(get("/admin/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/change-password"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void doChangePasswordSuccess() throws Exception {

        doNothing().when(userService).changePassword(any(), any());

        mockMvc.perform(post("/admin/change-password")
                .with(csrf())
                .param("currentPassword", "12345678")
                .param("newPassword", "123456789")
                .param("confirmPassword", "123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @TestConfiguration
    @Import(SecurityConfiguration.class)
    static class TestConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
            return new StubApplicationUserDetailsService(passwordEncoder);
        }
    }
}