package com.hoangluongtran0309.devblog.content.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.infrastructure.exception.NotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException(ArticleId articleId) {
        super("Article %s not found".formatted(articleId.asString()));
    }
}