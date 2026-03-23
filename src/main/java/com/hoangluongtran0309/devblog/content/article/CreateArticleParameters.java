package com.hoangluongtran0309.devblog.content.article;

import java.util.Set;

import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.TagId;

public class CreateArticleParameters {

    private final ArticleTitle articleTitle;
    private final ArticleSummary articleSummary;
    private final ArticleContent articleContent;
    private final ImagePreview imagePreview;
    private final Slug articleSlug;
    private final CategoryId categoryId;
    private final Set<TagId> tagIds;

    public CreateArticleParameters(ArticleTitle articleTitle, ArticleSummary articleSummary,
            ArticleContent articleContent, ImagePreview imagePreview, Slug articleSlug,
            CategoryId categoryId, Set<TagId> tagIds) {
        this.articleTitle = articleTitle;
        this.articleSummary = articleSummary;
        this.articleContent = articleContent;
        this.imagePreview = imagePreview;
        this.articleSlug = articleSlug;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
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

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public Set<TagId> getTagIds() {
        return tagIds;
    }
}