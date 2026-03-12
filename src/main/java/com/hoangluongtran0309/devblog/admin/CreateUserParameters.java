package com.hoangluongtran0309.devblog.admin;

public class CreateUserParameters {

    private final UserName userName;
    private final Email email;
    private final String password;

    public CreateUserParameters(UserName userName, Email email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UserName getUserName() {
        return userName;
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
