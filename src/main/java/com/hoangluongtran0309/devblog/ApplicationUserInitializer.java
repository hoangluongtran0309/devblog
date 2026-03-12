package com.hoangluongtran0309.devblog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hoangluongtran0309.devblog.admin.CreateUserParameters;
import com.hoangluongtran0309.devblog.admin.Email;
import com.hoangluongtran0309.devblog.admin.UserName;
import com.hoangluongtran0309.devblog.admin.UserService;

@Component
public class ApplicationUserInitializer implements CommandLineRunner {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final UserService userService;

    public ApplicationUserInitializer(
            @Value("${devblog.admin.first-name}") String firstName,
            @Value("${devblog.admin.last-name}") String lastName,
            @Value("${devblog.admin.email}") String email,
            @Value("${devblog.admin.password}") String password,
            UserService userService) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.exists(new Email(email))) {
            return;
        }

        userService.createAdministrator(
                new CreateUserParameters(new UserName(firstName, lastName), new Email(email), password));
    }
}
