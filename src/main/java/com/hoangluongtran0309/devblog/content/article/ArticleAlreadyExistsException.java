package com.hoangluongtran0309.devblog.content.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.CONFLICT)
public class ArticleAlreadyExistsException extends BusinessException {

    public ArticleAlreadyExistsException(Slug articleSlug) {
        super("Article with slug '%s' already exists".formatted(articleSlug.asString()));
    }

    public ArticleAlreadyExistsException(ArticleTitle articleTitle) {
        super("Article with title '%s' already exists".formatted(articleTitle.asString()));
    }
}