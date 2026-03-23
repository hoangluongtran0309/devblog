package com.hoangluongtran0309.devblog.content.article.web;

import java.util.Set;

import com.hoangluongtran0309.devblog.content.category.CategoryId;
import com.hoangluongtran0309.devblog.content.tag.TagId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AbstractArticleFormData {

    @NotBlank(message = "Article title must not be blank")
    @Size(max = 255, message = "Article title must not exceed 255 characters")
    private String articleTitle;

    @NotBlank(message = "Article summary must not be blank")
    @Size(max = 500, message = "Article summary must not exceed 500 characters")
    private String articleSummary;

    @NotBlank(message = "Article content must not be blank")
    private String articleContent;

    @Size(max = 500, message = "Image preview URL must not exceed 500 characters")
    @Pattern(regexp = "^(https?:\\/\\/.*)?$", message = "Image preview must be a valid URL")
    private String imagePreview;

    @NotBlank(message = "Article slug must not be blank")
    @Size(min = 3, max = 255, message = "Slug must be between 3 and 255 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String articleSlug;

    @NotNull(message = "Category must not be null")
    private CategoryId categoryId;

    @NotEmpty(message = "At least one tag is required")
    private Set<@NotNull(message = "Tag must not be null") TagId> tagIds;

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleSummary() {
        return articleSummary;
    }

    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(String imagePreview) {
        this.imagePreview = imagePreview;
    }

    public String getArticleSlug() {
        return articleSlug;
    }

    public void setArticleSlug(String articleSlug) {
        this.articleSlug = articleSlug;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryId categoryId) {
        this.categoryId = categoryId;
    }

    public Set<TagId> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<TagId> tagIds) {
        this.tagIds = tagIds;
    }
}