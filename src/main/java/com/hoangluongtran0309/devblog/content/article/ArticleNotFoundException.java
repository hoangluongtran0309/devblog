package com.hoangluongtran0309.devblog.content.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.infrastructure.exception.NotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException(ArticleId articleId) {
        super("Article %s not found".formatted(articleId.asString()));
    }

    public ArticleNotFoundException(Slug articleSlug) {
        super("Article %s not found".formatted(articleSlug.asString()));
    }
}