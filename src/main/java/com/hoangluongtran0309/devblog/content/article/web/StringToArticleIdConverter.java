package com.hoangluongtran0309.devblog.content.article.web;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.hoangluongtran0309.devblog.content.article.ArticleId;

@Component
public class StringToArticleIdConverter implements Converter<String, ArticleId> {

    @Override
    public ArticleId convert(String source) {
        return new ArticleId(UUID.fromString(source));
    }
}
