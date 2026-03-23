package com.hoangluongtran0309.devblog.content.article;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoangluongtran0309.devblog.content.shared.Slug;

public interface ArticleService {

    boolean existsBySlug(Slug articleSlug);

    boolean existsByTitle(ArticleTitle articleTitle);

    Optional<Article> getBySlug(Slug articleSlug);

    Optional<Article> getById(ArticleId articleId);

    Page<Article> getPublishedArticles(Pageable pageable);

    Page<Article> getPublishedArticlesByCategory(Slug categorySlug, Pageable pageable);

    Page<Article> getPublishedArticlesByTag(Slug tagSlug, Pageable pageable);

    Page<Article> searchPublishedArticles(String keyword, Pageable pageable);

    Page<Article> getAll(Pageable pageable);

    Article create(CreateArticleParameters parameters);

    Article update(ArticleId articleId, UpdateArticleParameters parameters);

    Article publish(ArticleId articleId);

    void delete(ArticleId articleId);
}
