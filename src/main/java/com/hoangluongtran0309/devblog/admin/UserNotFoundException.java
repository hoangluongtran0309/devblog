package com.hoangluongtran0309.devblog.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.NotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(UserId userId) {
        super("User %s not found".formatted(userId.asString()));
    }
}