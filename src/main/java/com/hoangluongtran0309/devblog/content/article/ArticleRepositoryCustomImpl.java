package com.hoangluongtran0309.devblog.content.article;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    @Override
    public ArticleId nextId() {
        return ArticleId.generate();
    }
}
