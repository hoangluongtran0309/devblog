package com.hoangluongtran0309.devblog.content.tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.NotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagNotFoundException extends NotFoundException {

    public TagNotFoundException(TagId tagId) {
        super("Tag %s not found".formatted(tagId.asString()));
    }
}