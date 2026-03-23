package com.hoangluongtran0309.devblog.content.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidArticleStatusException extends BusinessException {

    public InvalidArticleStatusException(ArticleId articleId, ContentStatus current) {
        super("Article %s has invalid status: %s"
                .formatted(articleId.asString(), current.name()));
    }
}