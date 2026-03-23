package com.hoangluongtran0309.devblog.content.tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.CONFLICT)
public class TagAlreadyExistsException extends BusinessException {

    public TagAlreadyExistsException(Slug tagSlug) {
        super("Tag with slug '%s' already exists".formatted(tagSlug.asString()));
    }

    public TagAlreadyExistsException(TagName tagName) {
        super("Tag with title '%s' already exists".formatted(tagName.asString()));
    }
}
