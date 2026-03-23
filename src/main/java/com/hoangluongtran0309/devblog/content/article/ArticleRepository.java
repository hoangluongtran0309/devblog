package com.hoangluongtran0309.devblog.content.article;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.Slug;

@Repository
@Transactional(readOnly = true)
public interface ArticleRepository extends CrudRepository<Article, ArticleId>, ArticleRepositoryCustom,
        PagingAndSortingRepository<Article, ArticleId> {

    boolean existsByArticleSlug(Slug articleSlug);

    boolean existsByArticleTitle(ArticleTitle articleTitle);

    Optional<Article> findByArticleSlug(Slug articleSlug);

    Page<Article> findByContentStatusOrderByPublishedAtDesc(ContentStatus contentStatus, Pageable pageable);

    Page<Article> findByCategory_CategorySlugAndContentStatusOrderByPublishedAtDesc(
            Slug categorySlug,
            ContentStatus contentStatus,
            Pageable pageable);

    Page<Article> findByTags_TagSlugAndContentStatusOrderByPublishedAtDesc(
            Slug tagSlug,
            ContentStatus status,
            Pageable pageable);

    Page<Article> findByArticleTitle_ArticleTitleContainingIgnoreCaseAndContentStatusOrderByPublishedAtDesc(
            String keyword,
            ContentStatus contentStatus,
            Pageable pageable);
}
