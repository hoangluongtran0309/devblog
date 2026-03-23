package com.hoangluongtran0309.devblog.content.article;

import java.util.UUID;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseId;

import jakarta.persistence.Embeddable;

@Embeddable
public class ArticleId extends BaseId<UUID> {

    protected ArticleId() {

    }

    public ArticleId(UUID id) {
        super(id);
    }

    public static ArticleId generate() {
        return new ArticleId(UUID.randomUUID());
    }
}
