package com.hoangluongtran0309.devblog.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordNotMatchException extends BusinessException {

    public PasswordNotMatchException() {
        super("Current password is incorrect");
    }

    public static PasswordNotMatchException confirmPasswordNotMatch() {
        return new PasswordNotMatchException("New password and confirm password do not match");
    }

    private PasswordNotMatchException(String message) {
        super(message);
    }
}
