package com.hoangluongtran0309.devblog.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.NotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MediaNotFoundException extends NotFoundException {

    public MediaNotFoundException(MediaId mediaId) {
        super("Media %s not found".formatted(mediaId.asString()));
    }
}