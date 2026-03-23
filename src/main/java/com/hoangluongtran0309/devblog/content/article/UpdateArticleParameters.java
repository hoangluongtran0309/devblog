package com.hoangluongtran0309.devblog.content.article;

import java.util.Set;

import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.TagId;

public class UpdateArticleParameters extends CreateArticleParameters {

    private final long version;

    public UpdateArticleParameters(long version, ArticleTitle articleTitle, ArticleSummary articleSummary,
            ArticleContent articleContent, ImagePreview imagePreview, Slug articleSlug, CategoryId categoryId,
            Set<TagId> tagIds) {
        super(articleTitle, articleSummary, articleContent, imagePreview, articleSlug, categoryId, tagIds);
        this.version = version;
    }

    public long getVersion() {
        return version;
    }
}
