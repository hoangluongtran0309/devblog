package com.hoangluongtran0309.devblog.infrastructure.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}