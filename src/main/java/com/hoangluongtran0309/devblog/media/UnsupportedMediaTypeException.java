package com.hoangluongtran0309.devblog.media;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedMediaTypeException extends BusinessException {

    public UnsupportedMediaTypeException(String type, Set<String> allowed) {
        super("Unsupported file type: %s. Allowed types: %s"
                .formatted(type, allowed));
    }
}