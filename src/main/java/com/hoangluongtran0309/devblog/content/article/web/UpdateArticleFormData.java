package com.hoangluongtran0309.devblog.content.article.web;

import java.util.stream.Collectors;

import com.hoangluongtran0309.devblog.content.article.Article;
import com.hoangluongtran0309.devblog.content.article.ArticleContent;
import com.hoangluongtran0309.devblog.content.article.ArticleSummary;
import com.hoangluongtran0309.devblog.content.article.ArticleTitle;
import com.hoangluongtran0309.devblog.content.article.UpdateArticleParameters;
import com.hoangluongtran0309.devblog.content.shared.ImagePreview;
import com.hoangluongtran0309.devblog.content.shared.Slug;
import com.hoangluongtran0309.devblog.content.tag.Tag;

public class UpdateArticleFormData extends AbstractArticleFormData {

    private String id;
    private long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public UpdateArticleParameters toParameters() {
        return new UpdateArticleParameters(
                version,
                new ArticleTitle(getArticleTitle()),
                new ArticleSummary(getArticleSummary()),
                new ArticleContent(getArticleContent()),
                new ImagePreview(getImagePreview()),
                new Slug(getArticleSlug()),
                getCategoryId(),
                getTagIds());
    }

    public static UpdateArticleFormData fromData(Article article) {
        UpdateArticleFormData result = new UpdateArticleFormData();

        result.setId(article.getId().asString().toString());
        result.setArticleTitle(article.getArticleTitle().asString());
        result.setArticleSummary(article.getArticleSummary().asString());
        result.setArticleContent(article.getArticleContent().asString());
        result.setImagePreview(article.getImagePreview().asString());
        result.setArticleSlug(article.getArticleSlug().asString());
        result.setCategoryId(article.getCategory().getId());
        result.setTagIds(article.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        result.setVersion(article.getVersion());

        return result;
    }
}
