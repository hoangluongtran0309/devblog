package com.hoangluongtran0309.devblog.content.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryAlreadyExistsException extends BusinessException {

    public CategoryAlreadyExistsException(Slug categorySlug) {
        super("Category with slug '%s' already exists".formatted(categorySlug.asString()));
    }

    public CategoryAlreadyExistsException(CategoryName categoryName) {
        super("Category with title '%s' already exists".formatted(categoryName.asString()));
    }
}
