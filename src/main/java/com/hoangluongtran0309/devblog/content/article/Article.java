package com.hoangluongtran0309.devblog.content.article;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.hoangluongtran0309.devblog.content.category.Category;
import com.hoangluongtran0309.devblog.content.shared.ContentStatus;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;
import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "db_article")
public class Article extends BaseEntity<ArticleId> {

    @Embedded
    @NotNull
    private ArticleTitle articleTitle;

    @Embedded
    @NotNull
    private ArticleSummary articleSummary;

    @Embedded
    @NotNull
    private ArticleContent articleContent;

    @Embedded
    @NotNull
    private ImagePreview imagePreview;

    @Embedded
    @AttributeOverride(name = "slug", column = @Column(name = "article_slug"))
    @NotNull
    private Slug articleSlug;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_status")
    private ContentStatus contentStatus;

    private LocalDateTime publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "article_tag", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    protected Article() {

    }

    public Article(ArticleId id, ArticleTitle articleTitle, ArticleSummary articleSummary,
            ArticleContent articleContent, ImagePreview imagePreview, Slug articleSlug,
            ContentStatus contentStatus, LocalDateTime publishedAt, Category category, Set<Tag> tags) {
        super(id);
        this.articleTitle = articleTitle;
        this.articleSummary = articleSummary;
        this.articleContent = articleContent;
        this.imagePreview = imagePreview;
        this.articleSlug = articleSlug;
        this.contentStatus = contentStatus;
        this.publishedAt = publishedAt;
        this.category = category;
        this.tags = tags;
    }

    public ArticleTitle getArticleTitle() {
        return articleTitle;
    }

    public ArticleSummary getArticleSummary() {
        return articleSummary;
    }

    public ArticleContent getArticleContent() {
        return articleContent;
    }

    public ImagePreview getImagePreview() {
        return imagePreview;
    }

    public Slug getArticleSlug() {
        return articleSlug;
    }

    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public Category getCategory() {
        return category;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
