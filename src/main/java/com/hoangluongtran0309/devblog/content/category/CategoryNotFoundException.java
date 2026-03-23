package com.hoangluongtran0309.devblog.content.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.exception.NotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(CategoryId categoryId) {
        super("Category %s not found".formatted(categoryId.asString()));
    }

    public CategoryNotFoundException(Slug categorySlug) {
        super("Category %s not found".formatted(categorySlug.asString()));
    }
}