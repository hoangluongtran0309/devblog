package com.hoangluongtran0309.devblog.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyFileException extends BusinessException {
    
    public EmptyFileException() {
        super("File cannot be empty");
    }
}